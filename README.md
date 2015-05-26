This is the Pugongying plugin for the Gradle.
This plugin, you can use the Pugongying API from Gradle easily.


## Usage
### Tasks
* uploadPgyer              - Uploads the APK file. Also updates the distribution specified by distributionKey if configured
* uploadPgyer[FlavorName]  - Upload an APK file of [FlavorName]

### Edit build.gradle

```
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'org.quanqi:pgyer:0.1.2'
  }
}
apply plugin: 'org.quanqi.pgyer'

pgyer {
    _api_key = ""
    uKey = ""
}

apks {
    release {
      sourceFile = file("[apk1 file path]")
    }
  }
}
```

### Run

```
$ ./gradlew uploadPgyer
```

## License
Copyright 2012-2014 DeployGate, henteko
Copyright 2015 Cindy, Jing Quanqi

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

```
http://www.apache.org/licenses/LICENSE-2.0
```
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
