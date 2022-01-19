# Dynatrace Perform Android HOT Session

# Code Snippets used in this session

## Start the OneAgent manually
```
Dynatrace.startup(this, new DynatraceConfigurationBuilder("<ApplicationID Placeholder>","<BeaconURL Placeholder>")
        .withUserOptIn(false)
        .withStartupLoadBalancing(true)
        .withCrashReporting(true)
        .buildConfiguration()
);
```


## Creating Custom Actions
```
public DTXAction createCustomAction(String actionName){
    DTXAction customAction = Dynatrace.enterAction(actionName);

    return customAction;
}
```

## Closing Custom Actions
```
public void closeCustomAction(DTXAction action){
    action.leaveAction();
}
```
    

## Creating Child Actions
```
public DTXAction createChildAction(DTXAction parentAction){
    DTXAction childAction = Dynatrace.enterAction("Child Action name",parentAction);

    return childAction;
}
```
   

## Tagging Sessions
```
public void tagSession(String userTag){
   Dynatrace.identifyUser(userTag);
}
```


## Tagging Web Requests
```
public void manualWebRequest(String url) {
    Thread thread = new Thread(new Runnable(){
        @Override
        public void run() {

            DTXAction webAction = Dynatrace.enterAction("Request Action");
            String uniqueRequestTag = webAction.getRequestTag();
            WebRequestTiming timing = Dynatrace.getWebRequestTiming(uniqueRequestTag);

            // Create the Request object with the URL and add our header tag
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(Dynatrace.getRequestTagHeader(), uniqueRequestTag);
                    .build();

            try {

                // Send the request
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()){
                    // Handle response
                }

                timing.stopWebRequestTiming(url, response.code(), response.message());

            } catch (IOException e) {
                timing.stopWebRequestTiming(url, -1, e.toString());
                e.printStackTrace();
            } finally {
                webAction.leaveAction()
            }


        }});
    thread.start();
}
```


## Reporting Events
```
public void reportEvent(DTXAction userAction){
    String event = "String value to report as a standalone event";

    
}
```


## Reporting Values
```
public void reportValue(DTXAction userAction) {
    int randomInteger = RAND.nextInt(); // use a randomly generated integer to report a value

}
```


## Reporting Errors
```
public void reportError(DTXAction userAction){
    try {
        URL url = new URL("httpSUPERSECRET::::::://////");
    } catch (MalformedURLException m) {
        m.printStackTrace();

    }
}
```

## Data Collection
```
public void setDataCollection(int level){

    DataCollectionLevel newLevel = DataCollectionLevel.USER_BEHAVIOR;
    switch(level){
        case OFF:
            newLevel = DataCollectionLevel.OFF;
            break;
        case PERFORMANCE:
            newLevel = DataCollectionLevel.PERFORMANCE;
            break;
        case USER_BEHAVIOR:
            newLevel = DataCollectionLevel.USER_BEHAVIOR;
            break;
    }
}
```



