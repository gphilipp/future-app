See doc on https://github.com/gphilipp/future-app/tree/master


`lein clean && lein prod-build && react-native run-ios --configuration Release`

This branch fails with
  
```
2017-07-03 11:26:34.149 [info][tid:main][RCTBatchedBridge.m:77] Initializing <RCTBatchedBridge: 0x6180001bc9a0> (parent: <RCTBridge: 0x6180002c05b0>, executor: RCTJSCExecutor)
2017-07-03 11:26:34.183 [warn][tid:com.facebook.react.JavaScript][RCTModuleData.mm:220] RCTBridge required dispatch_sync to load RCTDevSettings. This may lead to deadlocks
2017-07-03 11:26:49.655 [error][tid:com.facebook.react.JavaScript] Assert failed: c
2017-07-03 11:26:49.658 [fatal][tid:com.facebook.react.ExceptionsManagerQueue] Unhandled JS Exception: Assert failed: c
2017-07-03 11:26:49.684828+0200 FutureApp[28390:358646] [] nw_connection_get_connected_socket_block_invoke 3 Connection has no connected handler
2017-07-03 11:26:49.763 [info][tid:main][RCTRootView.m:295] Running application FutureApp ({
    initialProps =     {
    };
    rootTag = 1;
})
2017-07-03 11:26:49.764 [info][tid:com.facebook.react.JavaScript] Running application "FutureApp" with appParams: {"rootTag":1,"initialProps":{}}. __DEV__ === true, development-level warning are ON, performance optimizations are OFF
2017-07-03 11:26:49.764 [error][tid:com.facebook.react.JavaScript] Application FutureApp has not been registered.

Hint: This error often happens when you're running the packager (local dev server) from a wrong folder. For example you have multiple apps and the packager is still running for the app you were working on before.
If this is the case, simply kill the old packager instance (e.g. close the packager terminal window) and start the packager in the correct app folder (e.g. cd into app folder and run 'npm start').

This error can also happen due to a require() error during initialization or failure to call AppRegistry.registerComponent.


2017-07-03 11:26:49.766 [fatal][tid:com.facebook.react.ExceptionsManagerQueue] Unhandled JS Exception: Application FutureApp has not been registered.

Hint: This error often happens when you're running the packager (local dev server) from a wrong folder. For example you have multiple apps and the packager is still running for the app you were working on before.
If this is the case, simply kill the old packager instance (e.g. close the packager terminal window) and start the packager in the correct app folder (e.g. cd into app folder and run 'npm start').

This error can also happen due to a require() error during initialization or failure to call AppRegistry.registerComponent.
```
