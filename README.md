# MyShelfie
Java implementation of the board game **"MyShelfie"** by Cranio Creations as part of the Bachelor Thesis at PoliMi. The grade assigned to this project is: **30 / 30**.  
![](src/main/resources/gui/myShelfieImages/publisher_material/Display_1.jpg)
## Development
The software was developed using:
- **JDK**: Oracle OpenJDK version 17.0.6
- **Project Management**: Maven version 3.8.1
- **GUI**: JavaFX version 19.0.2.1 and SceneBuilder
- **IDE**: IntelliJ IDEA 2022.3.2

## How to run it
Download the jar file [here](https://github.com/benny-lo/IS23-AM38/blob/master/deliverables/jar/MyShelfie.jar).

### Server
` java -jar MyShelfie.jar server [{hostName} {rmiPort} {socketPort}] `

If the user omits the optional parameters, the server will be on localhost and the ports will be 1099 for RMI and 1234 for the sockets.

### Client
` java -jar MyShelfie.jar client {cli | gui} {tcp | rmi} [{hostName} {numberPort}] ` 

If the user omits the optional parameters, the client will automatically try to connect localhost with port 1099 for rmi and 1234 for the sockets.

## Implemented Functionalities
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Chat | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Resilience to disconnections | [![PURPLE](https://placehold.it/15/bb44bb/bb44bb)](#) |

>[![RED](https://placehold.it/15/f03c15/f03c15)](#) = not implemented <br>
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) = work in progress <br>
[![PURPLE](https://placehold.it/15/bb44bb/bb44bb)](#) = implemented after final exam (just for fun) <br>
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) = completed
>

## Authors
- Lorenzo Benedetti ([@benny-lo](https://github.com/benny-lo))
- Vincenzo De Masi ([@RayNup](https://github.com/RayNup))
- Nicola Bomben ([@Andandreigon](https://github.com/Andandreigon))
- Pietro Massari ([@pietromassari](https://github.com/pietromassari))

## License
The graphic material was licensed by Cranio Creations Srl.

NOTA: *My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.*

