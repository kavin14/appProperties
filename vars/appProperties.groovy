def call(){
echo "hello world"
buildTool="maven";
buildProp="";
buildHome="";

def getBuildTool(){
    return buildTool;
}

def getBuildProp(){
    return buildProp;
}

def getBuildHome(){
    return buildHome;
}

}
return this;
