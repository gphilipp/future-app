# Clojurescript with React-native advanced compilation


I'm trying to use :advanced mode to compile more efficient javascript. 


There are 3 branches:

- `master`: this is the baseline. The app is unmodified after using `re-natal init`, so it uses `:simple` optimizations
- `using_goog_object`: use `goog.object` instead of `.-someprop` to access javascript object properties as [suggested by @pesterhazy on #cljsrn](https://clojurians.slack.com/archives/C0E1SN0NM/p1499027721025230)
- `react_native_extern`: use https://github.com/artemyarulin/react-native-externs as [suggested by @mfikes on #cljsrn](https://clojurians.slack.com/archives/C0E1SN0NM/p1499038866853638)
  
`lein clean && lein prod-build && react-native run-ios --configuration Release`  
  This branch works perfectly.

