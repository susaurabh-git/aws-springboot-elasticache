# aws-springboot-elasticache
This is springboot REST application which will use AWS elasticache to Retrieve, Create, Update and Delete User object.

## Creating Elasticache instance

The first step to create the Elasticache instance is to create Security Group that will allow other services within the VPC to access the cache.
```shell
aws ec2 create-security-group --group-name spring-elasticache-sg \
--description "Allow access to Elasticache on deafult port (6379)" \
--vpc-id {your-vpc-id} 
```
Copy the security group id created and set "SECURITY_GROUP" environment variable so that CLI can use to automatically.
If we don't export it then we will have to provide it in each commands.
```shell
export SECURITY_GROUP= {sg-id-created-in-previous-command}
```
After Security Group we need to add Allow rule to allow other services within VPC to access Elasticache instance.
We can use following command to create Allow rule or AWS console.

```shell
aws ec2 authorize-security-group-ingress \
--group-id $SECURITY_GROUP --protocol tcp \
--port 6379 --cider {your-vpc-cider}
```

Now we need to create subnet group using following command

```shell
aws elasticache create-cache-subnet-group \
    --cache-subnet-group-name "spring-redis-cache-subnet" \
    --cache-subnet-group-description "elasticache subnet group for spring implementation " \
    --subnet-ids "subnet-xxxxec4f"
```

Once cache subnet group is created then we can create Elasticache instance using following command:
```shell
aws elasticache create-cache-cluster \
    --cache-cluster-id "spring-redis-cluster" \
    --cache-node-type cache.m3.medium \
    --engine redis \
    --engine-version 6.x \
    --port 6379 \
    --num-cache-nodes 1 \
    --cache-subnet-group-name  spring-redis-cache-subnet \
    --security-group-ids $SECURITY_GROUP
```
This command will take few minutes to create the cluster, once cluster is created and its statuc become available.
we can the see the status on AWS console or using following command:

```shell
aws elasticache describe-cache-clusters --cache-cluster-id spring-redis-cluster --show-cache-node-info
```
We can find elasticache Address and Port inside "Endpoint" details from above command output.

Elasticache configuration is complete and ready to use.

## Pre-requisites
- Java 11+
- Maven 3.6+
- AWS CLI 2
- Serverless framework CLI

## Build 
```shell
git clone https://github.com/susaurabh-git/aws-springboot-elasticache.git
cd aws-springboot-elasticache
mvn clean package
```

## Deploy

Set REDIS_HOST environment variable to be available for CLI commands
```shell
export REDIS_HOST=spring-redis-cluster.xxxxxx.xxxx.xxxx.cache.amazonaws.com
```
Serverless deploy
```shell
sls deploy
```
Grab URL from the output of above command. i.e.: `https://xxxxxx.execute.api.{region}.amazonaws.com/dev/{proxy+}`

## Test the Application

### Create User
```shell
curl -L -X POST 'https://xxxxxx.execute.api.{region}.amazonaws.com/dev/users' -H 'Content-Type: application/json'\
--data-raw '{
    "firstName":"John",
    "lastName":"Doe"
    }'
```

### Retrieve User
Get ID from above output
```shell
curl -L -X GET 'https://xxxxxx.execute.api.{region}.amazonaws.com/dev/users/{id-from-above-output}'
```

### Delete User
```shell
curl -L -X DELETE 'https://xxxxxx.execute.api.{region}.amazonaws.com/dev/users/{id-from-above-output}'
```