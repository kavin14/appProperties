#!/usr/bin/env groovy
import hudson.model.*
import hudson.EnvVars
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL
import groovy.json.JsonSlurper

def call(build_Type,current_Stage) {
 curlCommand = 'curl  -H "Accept: application/json" -H "Content-Type: application/json" -u admin:admin '
 buildUrlQuery = "api/json?tree=number,duration,timestamp,id,result"
 buildDetails = "$curlCommand$env.BUILD_URL$buildUrlQuery".execute().text
 def jsonSlurper = new JsonSlurper()
 def object = jsonSlurper.parseText(buildDetails)
 assert object instanceof Map
 
 TimeZone.getTimeZone('UTC')
 Date date= new Date()
 String newdate=date.format("YYYY-MM-DD HH:mm:ss.Ms")
 def buildType = build_Type;
 def currentStage = current_Stage;
 def jsonSlurper = new JsonSlurper()
 def object = jsonSlurper.parseText(x)
 def assert object instanceof Map
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
            "result": "${object.result}",
            "@timestamp": "$newdate",
            "build_type" : "$buildType",
            "stage": "$currentStage",
            "duration": ${object.duration}
        }
"""
["curl", "-i", "-XPOST", 
         "-H 'Content-Type:application/json'", 
         "-d ${deployJson}", 
         "http://10.20.28.90:9201/wf-build-data/jobdata/"].execute().text
}
