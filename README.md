FUNTEST
=======

Introduction
------------

FUNTEST is a functional test harness that is designed to use DSTL (domain specific test languages) to describe the functional test case

This repository is designed to replace the original seperate repositories originally stored in bitbucket. It contains multiple eclipse projects (as described below).

--------------------------------

Branches
--------

There are two special branches
   * __master__ - This will contain all released versions of the software - Tags will be used to denote versions
   * __int__ - This is the branch that all proposed changes for next release will be pulled to.

Building
--------

Each project has its own POM and therefore MAVEN can be used to build the jars. To build all jars at once see TestHarnessBuild project below

--------------------------------

Projects
--------

### HelperLibrary

Contains all of the helper classes that provide the actual functioinality of the harness.

### Launchers

These contain the different launchers for the harness - i.e. allows different launchers to be constructed for differnt purposes (e.g. command line running, CI server (such as jenkins)

### TestCaseLoader

This provides the mechanisim for interpreting the DSTL (domain specific test language) into instances of the TestStep interface

### TestFunctions

Provides a library of functions that can be used when performing variable replacement

### TestHarness

Contains the actual implementation of the test harness (split into seperate library to get over cyclic dependency issue)

### TestHarnessCore

Contains the core components of the test harness (such as Interfaces and Exceptions).

### TestOutputterLibrary

Provides a mechanisim for reporting the outcome of the test cases being run

### TestStepLibrary

Contains the instances of the TestStep interface that the harness uses to run through a test case.

### TestHarness-all

A project to hold a POM file to build a jar that contains all components of the above libraries

### TestHarnessBuild

A project to hold a POM file that is used to build all of the sub projects in the correct order. To build just run maven goal install on this project - This will install the built jars to your local repository

--------------------------------

Defects/Change Requests
-----------------------

Please use the issue tracker available in this project to track issues [avaialbe here](https://github.com/codersparks/funtest/issues)

--------------------------------

User Guide
----------

The user guide will eventually be placed into the [Project Wiki](https://github.com/codersparks/funtest/wiki)