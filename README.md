# Eriantys

Progetto di Ingegneria del Software A.A. 2021-2022

## Features

|     Feature      | Implemented                    |
|:----------------:|--------------------------------|
| Regole Complete  | ✅                              |
|       CLI        | ✅                              |
|       GUI        | ✅                              |
|       FA 1       | ✅ Persistence                  |
|       FA 2       | ✅ Resilience to disconnections |
|       FA 3       | ✅ Multiple games               |

## Testing

| Package    | Line coverage |
|------------|---------------|
| model      | **95%**       |
| controller | **88%**       |

## How to run

### Prerequisites
- Java 17

### Run
Download the ```.jar``` file from the [release page](https://github.com/francesco-re-1107/ing-sw-2022-re-scopacasa-zappa/releases).

#### Server
```java -jar eriantys.jar server```

#### Client CLI
```java -jar eriantys.jar client --cli```

#### Client GUI
```java -jar eriantys.jar client --gui```

#### NOTE for Windows
Change OS [/s](https://www.urbandictionary.com/define.php?term=%2FS). For the best experience:
- [Enable UTF-8 support](https://stackoverflow.com/a/57134096) for command prompt
- Download and install [Windows Terminal](https://apps.microsoft.com/store/detail/windows-terminal/9N0DX20HK701)
- Set window size to 80x24
- Increase font size
- Colors may differ from the ones in macOS/Linux

## Screenshots

### CLI
![cli1](https://imgur.com/MUGCGzI)
![cli2](https://imgur.com/Wrhd1wp)

### GUI
![gui1](https://imgur.com/wfoYgga)
![gui2](https://imgur.com/cxGxkpy)