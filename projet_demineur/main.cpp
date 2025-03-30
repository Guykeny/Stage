#include <QApplication>
#include "projet.h"
#include <QTranslator>
#include <QLocale>
#include <QDebug>

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QTranslator translator;
    QLocale langue= QLocale::system();
    QString locale=langue.name();

    if(translator.load(locale + ".qm")){
         app.installTranslator(&translator);
    }

    MainWindow window;
    app.setWindowIcon(QIcon(":/img/annexes_projets_demineur/drapeau.png"));
    window.show();
    return app.exec();
}
