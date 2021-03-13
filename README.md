 
 [![Build Status](https://travis-ci.org/Philmasteryeah/quizmaker.svg?branch=master)](https://travis-ci.org/Philmasteryeah/quizmaker)
 
![alt text](https://abload.de/img/screenshot2021-02-1325gkcq.png)

# Quizmaker
- Spring Boot 2
- Spring Security
- Spring AOP
- Spring REST MVC
- Thymeleaf
- AdminLTE 3 (JQuery & bootstrap integrated) 
- webjars
- Tomcat (embedded)
- H2, JPA
- gradle

# The application
- backend for a quiz game application
- user login, management and registration
- create a Quiz with Questions and Answers
- play the quiz for testing inside the application
- serve the Quiz per REST for other Applications (e.g. Flutter App)
- import/export JSON Quiz Files

[![alt text](https://s4.gifyu.com/images/GIF-28.02.2021-01-53-26.gif)](https://gifyu.com/image/ZcL8)

# How to start
```
$ git clone https://github.com/Philmasteryeah/quizmaker.git
$ cd quizmaker
$
$ ./gradlew bootRun
```

# Why-tldr
- simple ADMIN LTE 3 one dependency
- less JavaScript, without Big JS Framework like VUE.js, react, or angular
- only Thymeleaf with Spring MVC, AOP Security,  Spring Beans, JQuery and Bootstrap
- fast good looking backend archetype, ready to start

# Why-long
- Admin LTE 3 uses Boootstrap 4 with JQuery so we need JQuery
- Bootstrap with Angular or Vue is totaly fail because of JQuery
- Bootstrap 5 dont uses JQuery -> this is the new way to go
- spring mvc is used for the internal pages made with thymeleaf
- the internal base pages are a minimal monolithic core for user administration and tests
- later without JQuery
- the REST Routes can be used to be consumed by a app or other apps later -> Quizapp



# Highlights completed
- simple full and nice looking Thymeleaf AdminLTE 3 Template
- all from almost one AdminLTE webjar inclusive AdminLTE Plugins
- login and registration page, error page included, all responsive
- less easy webjar dependencies with webjarlocator
- **full implemented working Spring User Login and Security with AOP (Aspect Oriented)**
- AOP injects security checks around annotated methods i.e findAll, save etc.
- the Access Control Service is the complete separation of the security stuff
- easy and clean code, nice for extending with own stuff (e.g. permissions)
- complete JPA Model see ERD below


### Dashboard /
![alt text](https://abload.de/img/screenshot2021-02-1320ojwd.png)

### Login /user/login
![alt text](https://abload.de/img/screenshot2021-02-132yvk73.png)

# Plugins used
- **full** usage see template.html, **minimal** usage see login.html
- chart.js/Chart.min.js
- jqvmap/jquery.vmap.min.js
- jqvmap/maps/jquery.vmap.usa.js
- jquery-knob/jquery.knob.min.js
- moment/moment.min.js
- daterangepicker/daterangepicker.js
- overlayScrollbars/js/jquery.overlayScrollbars.min.js
- sparklines/sparkline.js
- summernote/summernote-bs4.min.js
- tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js

# ERD Model
![alt text](https://abload.de/img/screenshot2021-02-14028kmg.png)



# TODOs
- user management
- quiz management
- Import / Export
- add tests with newest junit
- REST documentation Swagger
- deploy on heroku
- add banner to git
- use git project management stuff

# THX
- inspired by : https://github.com/oojorgeoo89/QuizZz
- AdminLTE dependencies https://adminlte.io/docs/3.0/dependencies.html
- AdminLTE Widgets https://adminlte.io/themes/v3/pages/widgets.html

