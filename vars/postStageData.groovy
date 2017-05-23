#!/usr/bin/env groovy
import hudson.model.*
import hudson.EnvVars
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL
import groovy.json.JsonSlurper

def call(){
     curlCommand = 'curl  -H "Accept: application/json" -H "Content-Type: application/json" -u admin:admin '
     buildUrlQuery = "wfapi/"
     buildDetails = "$curlCommand$env.BUILD_URL$buildUrlQuery".execute().text
     def jsonSlurper = new JsonSlurper()
     def object = jsonSlurper.parseText(buildDetails)
     assert object instanceof Map
     buildDetails = object

["curl", "-i", "-XPOST", 
         "-H 'Content-Type:application/json'", 
         "-d ${buildDetails}", 
         "http://10.20.28.90:9201/wf-build-stage-data/jobdata/"].execute().text
}

