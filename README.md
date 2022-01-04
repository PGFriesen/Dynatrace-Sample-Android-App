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

## Modify data collection levels
```
Dynatrace.applyUserPrivacyOptions(UserPrivacyOptions.builder()
        .withCrashReportingOptedIn(true)
        .withDataCollectionLevel(DataCollectionLevel.USER_BEHAVIOR)
        .build()
);
```