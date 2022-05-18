package it.polimi.ingsw.client.cli.views;

public class WelcomeView extends BaseView {
    String logo = """
               ,ggggggg,                                                              \s
             ,dP""\"""\"Y8b                                       I8                    \s
             d8'    a  Y8                                       I8                    \s
             88     "Y8P'          gg                        88888888                 \s
             `8baaaa               ""                           I8                    \s
            ,d8P""\""     ,gggggg,  gg    ,gggg,gg  ,ggg,,ggg,   I8  gg     gg   ,g,   \s
            d8"          dP""\""8I  88   dP"  "Y8I ,8" "8P" "8,  I8  I8     8I  ,8'8,  \s
            Y8,         ,8'    8I  88  i8'    ,8I I8   8I   8I ,I8, I8,   ,8I ,8'  Yb \s
            `Yba,,_____,dP     Y8_,88,,d8,   ,d8b,dP   8I   Yb,d88b,d8b, ,d8I,8'_   8)\s
              `"Y8888888P      `Y8P""YP"Y8888P"`Y8P'   8I   `Y8P""YP""Y88P"88P' "YY8P8P
                                                                         ,d8I'        \s
                                                                       ,dP'8I         \s
                                                                      ,8"  8I         \s
                                                                      I8   8I         \s
                                                                      `8, ,8I         \s
                                                                       `Y8P"          \s""";

    @Override
    protected void draw() {
        cursor.clearScreen();
        cursor.printBold(logo);
        cursor.printPrompt("Username:");
    }
}
