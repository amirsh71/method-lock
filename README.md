# Method-Lock
Method-Lock help you to manage concurrent request to a method and lock operation on method base on given method parameters.
## Usage:
1. add lock dependency to your spring project:
```xml
<dependency>
    <groupId>com.amirsh71</groupId>
    <artifactId>method-lock</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. add package name of lock module to your spring componentScan:
```java
@ComponentScan(basePackages = "com.amirsh71.methodlock.core")
```

3. use of one the cache implementations (in-memory, redis, redis-cluster) or implement your cache manager yourself! 
4. put `@Lock` on your method and usage of the method will be locked for all concurrent request based on given parameters.


## Configuration:

* @Lock annotation fields:

    1. timeoutSec: timeout of lock in cache. if not set default value of overallTimeout will be used.

    2. type: a string key that for type of lock, for example: "`ORDER_FOOD`"

    3. params: an array of parameters for using in lock object, for example billId. you can use Spring Expression Language (`SpEL`) to use values of methods parameters (see examples)



* overall configurations:
  
    1. set redis host:port in properties file 
    2. to set timout globally set `methodlock.timoutSec` in properties file.
    3. to set application name in lock object, set `spring.application.name` in properties file
### Using In-memory cache management:
add `LockCacheInMemoryServiceImpl` as spring `bean` to application. 

* note: in-memory cache management doesn't support timout at the moment.
``` java
    @Bean
    public LockCacheService lockCacheService(){
        return new LockCacheInMemoryServiceImpl();
    }
```

## Exceptions:
* if method was lock, an Exception with type `OperationLockedException` will be thrown.
* if connection to cache has a problem, an Exception with type `LockingException` will be thrown.   
  
## examples:
```java
class Request{
    String userId;
    String foodId;
}

@Lock(type = "ORDER_FOOD", params = {"#request.userId","#request.foodId"}, timeoutSec = 4)
public ResponseDto<Void> lock(Request request) {
    ...
}
```
```java
class A{
    B b;
}
class B{
    long id;
}

@Lock(type = "SOMETHING", params = "#list.![b.id]")
public ResponseDto<Void> lockList(List<A> list) {
    return null;
}
```
```java
@PostMapping(value = "lockList22")
@Lock(type = "SOMETHING", params = {"#c.list.![b.id]","#param", "#param2"})
public ResponseDto<Void> lockList2(@RequestBody @Validated C c, @RequestParam Long param, @RequestParam Long param2) {
return null;
}
```
```java
@Lock(type = "JUST-BY-TYPE")
public ResponseDto<Void> lockList2(Long param, Long param2) {
return null;
}
```
## Redis Cache Implementation
See https://github.com/amirsh71/method-lock-redis for using redis as cache management.
## Redis Cluster Cache Implementation
See https://github.com/amirsh71/method-lock-redis-cluster for usign redis cluster as cache management.
## Sample Application
See https://github.com/amirsh71/method-lock-sample-application for some usage sample.