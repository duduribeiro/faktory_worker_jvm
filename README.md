# FaktoryWorker for JVM
(still in progress)

This library will handle the communication between JVM languages to [Faktory](http://contribsys.com/faktory/).

It will allows you to push and cosume Faktory's jobs to be processed.

## Examples:

### Java

#### Pushing a Job

```java
  FaktoryClient client = new FaktoryClient();
  FaktoryJob job = new FaktoryJob("MyJob", 1, 2, "foo");
  client.pushJob(job);
```
