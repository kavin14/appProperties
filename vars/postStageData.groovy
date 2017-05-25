#!/usr/bin/env groovy
import hudson.model.*
import hudson.EnvVars
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL
import groovy.json.JsonSlurper

def call(build_Type){
     def x = "$env.BUILD_URL".split('/')
     def jobendtime = new Date()
     curlCommand = 'curl  -H "Accept: application/json" -H "Content-Type: application/json" -u admin:admin '
     buildUrlQuery = "wfapi/"
     buildDetails = "$curlCommand$env.BUILD_URL$buildUrlQuery".execute().text
     def jsonSlurper = new JsonSlurper()
     def object = jsonSlurper.parseText(buildDetails)
     assert object instanceof Map
     object << [buildType: build_Type]
     //object << [masterName: x[3].replace('-','')]
     object << [masterName: x[3]]
     object << [JobName: x[5]]
     object << [timestamp: jobendtime]
      if(currentBuild.result != "FAILIURE" || currentBuild.result != "ABORTED") {
         currentBuild.result = "SUCCESS"
     }
     object << [buildStatus: currentBuild.result]
     buildDetails = JsonOutput.toJson(object)
    
["curl", "-i", "-XPOST", 
         "-H 'Content-Type:application/json'", 
         "-d ${buildDetails}", 
         "http://10.20.28.90:9201/wf-build-data-test/jobdata/"].execute().text
}

