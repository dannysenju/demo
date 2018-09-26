# demo



## Usage

Build locally
```console
$ git clone https://github.com/dannysenju/demo.git
$ cd demo
$ mvn clean install
```
Setup an heroku app to use wildfly and postgresql database driver.

```console
$ heroku create
$ heroku addons:create heroku-postgresql:hobby-dev
$ heroku buildpacks:clear
$ heroku buildpacks:add heroku/java
$ heroku buildpacks:add https://github.com/mwiede/heroku-buildpack-wildfly
$ heroku buildpacks:add https://github.com/mwiede/heroku-buildpack-wildfly-postgresql
```

Deploy
```console
$ git push heroku master
```
