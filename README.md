# i-love-zappos
## About
Such is a v interesting coding project handed over by the secret agents of Zappos.com. In short, the task is to build a native Android app that have the following functions: 1. display transaction history; 2. display an order book with bids and asks; 3. set customized price alert. Nice.
## Table of Contents
* [About](#about)
* [Folders Explained](#folders-explained)
  * [app](#app)
  * [misc](#misc)
* [Examples](#examples)
* [Bug Fix](#bug-fix)

## Folders Explained
### app
This folder has the source used to build the Coin Zap app. 
* app/src/main/java/sophiehuang/ilovezappos 
  * it contains the main activity with 3 fragments class 
  * it also contains a folder named "Model", where MPAndroidChart, Retrofit2, RecyclerAdapter, JobDispatcher classes go in.

* app/src/main/res/ - it has all the layouts and icons materials and xml files

### misc
This folder contains some testing and debug xml files automatically uploaded from Android Studio while committing and pushing to Github. 

## Examples

Here some screenshots provided:

![Transaction History Fragment]()


![Order Book Fragment]()


![Price Alert Fragment]()


## Bug Fix

There is an unknown bug that makes the markerView within the line chart unstable. Sometimes the marker shows up but sometimes it doesn't. I have tried to find the reasons for a long time but so far I have no luck. 
