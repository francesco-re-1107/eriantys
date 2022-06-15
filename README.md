# Eriantys

Progetto di Ingegneria del Software A.A. 2021-2022

## Features

| Feature                            | Implemented  |
|------------------------------------|:------------:|
| Regole Complete                    |      ✅       |
| CLI                                |      ✅       |
| GUI                                |      ✅       |
| Multiple games (FA1)               |      ✅       |
| Persistence (FA2)                  |      ✅       |
| Resilience to disconnections (FA3) |      ✅       |

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
```
java -jar eriantys.jar server
```

Deployed server: ```eriantys.francescore.dev:6001``` ![Uptime Robot status](https://img.shields.io/uptimerobot/status/m792016027-a3a6d8ca3c90667d373c5137)

Server configuration

| Env variable                 | Description               | Default value                   |
|------------------------------|---------------------------|---------------------------------|
| ```ERIANTYS_SERVER_PORT```   | Server port               | ```6001```                      |
| ```ERIANTYS_BACKUP_FOLDER``` | Persistence backup folder | ```Current Working Directory``` |

#### Client CLI
```
java -jar eriantys.jar client --cli
```

#### Client GUI
```
java -jar eriantys.jar client --gui
```

#### NOTE for Windows
For the best experience:
- Change OS [/s](https://www.urbandictionary.com/define.php?term=%2FS)
- [Enable UTF-8 support](https://stackoverflow.com/a/57134096) for command prompt
- Download and install [Windows Terminal](https://apps.microsoft.com/store/detail/windows-terminal/9N0DX20HK701)
- Set window size to 80x24
- Increase font size
- Colors may differ from the ones in macOS/Linux

## Screenshots

### CLI
![cli1](https://i.imgur.com/MUGCGzI.png)
![cli2](https://i.imgur.com/Wrhd1wp.png)

### GUI
![gui1](https://i.imgur.com/wfoYgga.png)
![gui2](https://i.imgur.com/cxGxkpy.png)