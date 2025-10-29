[<img src="https://cdn-mobile.actito.com/logo.png"/>](https://actito.com)

# Actito Services Gradle Plugin

[![GitHub release](https://img.shields.io/github/v/release/actito/actito-services-gradle-plugin)](https://github.com/actito/actito-services-gradle-plugin/releases)
[![License](https://img.shields.io/github/license/actito/actito-services-gradle-plugin)](https://github.com/actito/actito-services-gradle-plugin/blob/main/LICENSE)

A Gradle plugin for adding the Actito Services configuration file to Android projects.

Table of contents
=================

* [Installation](#installation)
    * [Compatibility](#compatibility)
    * [Configuration](#configuration)
      * [Plugins DSL](#plugins-dsl)
      * [Legacy Plugin Application](#legacy-plugin-application)
* [Usage](#usage)

## Installation

### Compatibility

- Compatible with AGP 8.0.0 or newer ([Android Gradle Plugin](https://developer.android.com/build/releases/gradle-plugin)) and Gradle 8.0 or newer.

### Configuration

#### Plugins DSL

Add the Gradle plugin repository to your app's `settings.gradle`

```gradle
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
```

Add the Actito Services Gradle Plugin to your project's `build.gradle`:

```gradle
plugins {
    id 'com.actito.gradle.actito-services' version '1.0.0' apply false
}
```

Apply the plugin on your app's `build.gradle`:

```gradle
plugins {
    id 'com.actito.gradle.actito-services'
}
```

#### Legacy Plugin Application

Add the Actito Services Gradle Plugin to your project's `build.gradle` dependencies:

```gradle
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath 'com.actito.gradle:actito-services:1.0.0'
    }
}
```

Apply the plugin on your app's `build.gradle`:

```gradle
apply plugin: 'com.actito.gradle.actito-services'
```

## Usage

To connect your app with Actito:
- Place the provided `actito-services.json` configuration file inside your app module (e.g. `app/actito-services.json`).
- The plugin will automatically detect the file and configure the Actito SDK for you.
