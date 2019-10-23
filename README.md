# Aviation Data

[![CircleCI](https://circleci.com/gh/vinicius-pmoleta/aviation-data.svg?style=svg&circle-token=7ad79d72c0bb17b0cbf52455e9f73360d218040d)](https://circleci.com/gh/vinicius-pmoleta/aviation-data)

## About

Aviation Data is an app to study and apply modern Android development practices, libraries and tendencies.

The app is structured to be multi-module and leverage `Kotlin Coroutines` in a MVVM architecture with states and Android Architecture Components.

## Overview

The app consists in two screens, one to search and list for aircraft based on its model or registration and another to see a gallery with the selected aircraft photos.

In order to provide the above-mentioned information the [OpenSky Network](https://opensky-network.org/) API was used to provide the search results and the [JetPhotos](https://www.jetphotos.com/) was used to provide the photo gallery.

## Modules

### Common

#### Core

#### Core Test

#### Core Android Test

#### Navigation

### Feature

A feature module was designed to be an Android Library that makes use of the common modules when needed. It groups one or more screens that logically makes sense to be together.

At the moment it doesn't make use of the `Android Dynamic Modules` from which we could leverage dynamic feature deliver and smaller APKs. The reason for that is that I couldn't make it work properly with the unit and instrumented tests in a multi-module architecture. In order to proceed with other study topics I decided to address it in a later moment, but an initial attempt to use them can be found at this [branch](https://github.com/vinicius-pmoleta/aviation-data/tree/improvement/adding-dynamic-modules).

Each feature module relies on the usage of `Kotlin Coroutines`, `Kodein DI` and a MVVM architecture with states and AACs such as `ViewModel` and `LiveData`.

The option to use the `Kotlin Coroutines` instead of the common approach to use `RxJava` as an attempt to simplify multi-threading operations which very often can lead to very complicated data streams that are hard to understand, debug and maintain. In this specific study project there wasn't the need of a data stream across different layers so it was one more reason `Kotlin Coroutines` was chosen over `RxJava`, but if there was a need for data stream a study and attempt would be also made with the recent `Kotlin Flow` to compare its pros and cons against `RxJava`.

Another decision made was to use `Kodein` instead of the common approach to use `Dagger`. Again one of the reasons to choose Kodein is to explore less verbose options which can provide similar capabilities and also allow an easier injection override on tests to allow screen and integration tests. 

Each feature developments was broken down into the following layers:

#### Data:
#### Business:
#### ViewModel:
#### View:

### Application

This is the main app module, which due to the decision of making the feature modules as Android libraries, will depend on the the feature modules as well as some common modules such as `core` and `navigation`.

## Building and running

## Credits

While studying and building this application many different videos and blog posts were very useful:
- https://jeroenmols.com/blog/2019/06/12/modularizationtips
- https://github.com/dotanuki-labs/norris
- https://medium.com/androiddevelopers/dependency-injection-in-a-multi-module-project-1a09511c14b7
- https://proandroiddev.com/android-architecture-d7405db1361c

## License

```
MIT License

Copyright (c) 2019 Vinicius Pilot Moleta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```