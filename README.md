# Implementing a generic Play filter in Lagom Scala 

1. In your `LagomApplication`, override the `lazy val httpFilters` (if you aren't overriding it already) and include your filter. Note that the type of httpFilters must be Seq[EssentialFilter].
2. Implement your filter. Note that it might be easier to extend play.api.mvc.Filter as it handles much of the necessary type conversion between the java and scala objects.

## Testing the filter

Run the ./test.sh script in the project root directory. It makes two requests using curl, one with and without the requisite headers. You can see the difference in the server responses...
You can test this recipe using 2 separate terminals.

Example curl request...
```
curl -H "Access-Control-Request-Method: GET" \
        -H "Access-Control-Request-Headers: origin, x-requested-with" \
        -H "Origin: http://www.some-domain.com"  \
        http://localhost:9000/api/hello/123 -v        
```

Note how the request uses the `OPTIONS` method and targets the [Lagom Service Gateway](https://www.lagomframework.com/documentation/1.3.x/scala/ServiceLocator.html) (`localhost:9000`).

## More resources

This topic has been discussed in the Lagom Mailing List [a](https://groups.google.com/forum/?utm_medium=email&utm_source=footer#!msg/lagom-framework/_3Hjvp18NNU/ygu8Pa5wAQAJ) [few](https://groups.google.com/forum/?utm_medium=email&utm_source=footer#!msg/lagom-framework/7YZccqRUS4g/HNMykAiGBAAJ) [times](https://groups.google.com/forum/?utm_medium=email&utm_source=footer#!msg/lagom-framework/3y0wgIMillE/ItT1rPDfBgAJ), if this recipe doesn't resolve your doubts, feel free to ask for help in the community.

