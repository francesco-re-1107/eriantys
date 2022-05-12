package it.polimi.ingsw.client.cli.views;

public class WelcomeView extends BaseView {
    String logo = "   ,ggggggg,                                                               \n" +
            " ,dP\"\"\"\"\"\"Y8b                                       I8                     \n" +
            " d8'    a  Y8                                       I8                     \n" +
            " 88     \"Y8P'          gg                        88888888                  \n" +
            " `8baaaa               \"\"                           I8                     \n" +
            ",d8P\"\"\"\"     ,gggggg,  gg    ,gggg,gg  ,ggg,,ggg,   I8  gg     gg   ,g,    \n" +
            "d8\"          dP\"\"\"\"8I  88   dP\"  \"Y8I ,8\" \"8P\" \"8,  I8  I8     8I  ,8'8,   \n" +
            "Y8,         ,8'    8I  88  i8'    ,8I I8   8I   8I ,I8, I8,   ,8I ,8'  Yb  \n" +
            "`Yba,,_____,dP     Y8_,88,,d8,   ,d8b,dP   8I   Yb,d88b,d8b, ,d8I,8'_   8) \n" +
            "  `\"Y8888888P      `Y8P\"\"YP\"Y8888P\"`Y8P'   8I   `Y8P\"\"YP\"\"Y88P\"88P' \"YY8P8P\n" +
            "                                                             ,d8I'         \n" +
            "                                                           ,dP'8I          \n" +
            "                                                          ,8\"  8I          \n" +
            "                                                          I8   8I          \n" +
            "                                                          `8, ,8I          \n" +
            "                                                           `Y8P\"           ";

    @Override
    protected void init() {
        cursor.clearScreen();
        cursor.printBold(logo);
        cursor.printPrompt("Username:");
    }
}
