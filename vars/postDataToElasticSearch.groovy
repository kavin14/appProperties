#!/usr/bin/env groovy
def call(build_Type,current_Stage) {

 TimeZone.getTimeZone('UTC')
 Date date= new Date()
 String newdate=date.format("YYYY-MM-DD HH:mm:ss.Ms")
 def buildType = build_Type;
 def currentStage = current_Stage;
 def deployJson = """
  {
            "scm_branch": "git",
            "job_name": "$env.JOB_NAME",
            "job_url": "$env.JOB_URL",
            "build_url": "$env.BUILD_URL",
            "environment": "Test",
            "application_name": "$env.JOB_NAME",
            "artifact": "",
            "deploy_url": "$env.BUILD_URL",
            "result": "${currentBuild.result}",
            "@timestamp": "$newdate",
            "build_type" : "$buildType",
            "stage": "$currentStage"
        }
"""
["curl", "-i", "-XPOST", 
         "-H 'Content-Type:application/json'", 
         "-d ${deployJson}", 
         "http://10.20.28.90:9201/wf-build-data/jobdata/"].execute().text
 
 http.request( 'http://admin:admin@10.20.28.90:8081', GET, TEXT ) { req ->
  uri.path = '/jenkins-team-1/job/maven/21/api/json?tree=number,status,timestamp,id,result'
  headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
  headers.Accept = 'application/json'

  response.success = { resp, reader ->
    assert resp.statusLine.statusCode == 200
    println "Got response: ${resp.statusLine}"
    println "Content-Type: ${resp.headers.'Content-Type'}"
    println reader.text
  }

  response.'404' = {
    println 'Not found'
  }
}
}
