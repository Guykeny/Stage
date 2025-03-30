#include "projet.h"
#include <QMenuBar>
#include <QMenu>
#include <QAction>
#include <QFileDialog>
#include <QDir>
#include <QTime>
#include <QDebug>
#include <QApplication>
#include <QIcon>
#include <QRandomGenerator>
#include <QMessageBox>
#include <QInputDialog>
#include <QPainter>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent), tempsEcoule(0), bombesRestantes(0), tailleGrille(10), nombreBombes(10), gameActive(false),
      clickedMineRow(-1), clickedMineCol(-1)
{
    setWindowIcon(QIcon(":/img/annexes_projets_demineur/drapeau.png"));
    setupMenu();
    setupGameBoard();
}

MainWindow::~MainWindow()
{
    for (int row = 0; row < cells.size(); ++row) {
        for (int col = 0; col < cells[row].size(); ++col) {
            delete cells[row][col];
        }
    }
}

void MainWindow::setupMenu()
{
    QMenu *fileMenu = menuBar()->addMenu(tr("&File"));
    QAction *newGameAction = fileMenu->addAction(tr("&New Game"));
    QAction *quitAction = fileMenu->addAction(tr("&Quit"));
    connect(newGameAction, &QAction::triggered, this, &MainWindow::newGame);
    connect(quitAction, &QAction::triggered, qApp, &QApplication::quit);

    QMenu *scoresMenu = menuBar()->addMenu(tr("&Scores"));
    QAction *showScoresAction = scoresMenu->addAction(tr("&Show Scores"));
    connect(showScoresAction, &QAction::triggered, this, &MainWindow::showScores);

    QMenu *helpMenu = menuBar()->addMenu(tr("&Help"));
    QAction *aboutQtAction = helpMenu->addAction(tr("About &Qt"));
    QAction *aboutMinesweeperAction = helpMenu->addAction(tr("About &Minesweeper"));
    connect(aboutQtAction, &QAction::triggered, this, &MainWindow::showAboutQt);
    connect(aboutMinesweeperAction, &QAction::triggered, this, &MainWindow::showAboutMinesweeper);
}

void MainWindow::setupGameBoard()
{
    QWidget *centralWidget = new QWidget(this);
    setCentralWidget(centralWidget);

    gridLayout = new QGridLayout(centralWidget);
    gridLayout->setSpacing(0);
    gridLayout->setContentsMargins(0,0,0,0);

    timeLcd = new QLCDNumber(3, centralWidget);
    timeLcd->setSegmentStyle(QLCDNumber::Filled);
    timeLcd->display("000");
    gridLayout->addWidget(timeLcd, 0, 0, 1, 1);

    smileyButton = new QPushButton(centralWidget);
    smileyButton->setIcon(QIcon(":/img/annexes_projets_demineur/neutre.png"));
    gridLayout->addWidget(smileyButton, 0, 1, 1, 1);

    bombsLcd = new QLCDNumber(3, centralWidget);
    bombsLcd->setSegmentStyle(QLCDNumber::Filled);
    bombsLcd->display("010");
    gridLayout->addWidget(bombsLcd, 0, 2, 1, 1);


    cells.resize(tailleGrille);
    mines.resize(tailleGrille);
    revele.resize(tailleGrille);
   drapeau.resize(tailleGrille);

    for (int row = 0; row < tailleGrille; ++row) {
        cells[row].resize(tailleGrille);
        mines[row].resize(tailleGrille);
        revele[row].resize(tailleGrille);
       drapeau[row].resize(tailleGrille);

        for (int col = 0; col < tailleGrille; ++col) {
            QPushButton *cell = new QPushButton(centralWidget);
            cell->setFixedSize(30, 30);
            cell->setContextMenuPolicy(Qt::CustomContextMenu);
            gridLayout->addWidget(cell, row + 1, col);
            cells[row][col] = cell;
            mines[row][col] = false;
            revele[row][col] = false;
           drapeau[row][col] = false;

            connect(cell, &QPushButton::clicked, this, &MainWindow::onCellClicked);
            connect(cell, &QPushButton::customContextMenuRequested, this, &MainWindow::onCellRightClicked);
        }
    }

    bombesRestantes = nombreBombes;
    bombsLcd->display(QString::number(bombesRestantes).rightJustified(3, '0'));  // Format bombs with 3 digits
    timer = new QTimer(this);
    connect(timer, &QTimer::timeout, this, &MainWindow::updateTimer);
    gameActive = false;
}

void MainWindow::newGame()
{
    QStringList Options_de_difficulte = {tr("Easy - 10*10 with 10 bombs"), tr("Medium - 16*16 with 16 bombs"), tr("Hard - 32*16 with 64 bombs") } ;
    bool ok;
    QString ddifficulte = QInputDialog::getItem(this, tr("Choose Ddifficulte"), tr("Ddifficulte Level:"), Options_de_difficulte, 0, false, &ok);
    if (!ok) return;

    if (ddifficulte == "Easy - 10*10 with 10 bombs") {
        tailleGrille = 10;
        nombreBombes = 10;
    } else if (ddifficulte == "Medium - 16*16 with 16 bombs") {
        tailleGrille = 16;
        nombreBombes = 16;
    } else if (ddifficulte == "Hard - 32*16 with 64 bombs") {
        tailleGrille = 32;
        nombreBombes = 64;
    }

    for (int row = 0; row < cells.size(); ++row) {
        for (int col = 0; col < cells[row].size(); ++col) {
            delete cells[row][col];
        }
    }
    cells.clear();
    mines.clear();
    revele.clear();
   drapeau.clear();

    bombesRestantes = nombreBombes;
    tempsEcoule = 0;
    gameActive = false;

    bombsLcd->display(QString::number(bombesRestantes).rightJustified(3, '0'));
    timeLcd->display(tempsEcoule);
    smileyButton->setIcon(QIcon(":/img/annexes_projets_demineur/neutre.png"));

    setupGameBoard();
}

void MainWindow::generateMines(int firstClickRow, int firstClickCol)
{
    int minesPlaced = 0;
    while (minesPlaced < nombreBombes) {
        int row = QRandomGenerator::global()->bounded(tailleGrille);
        int col = QRandomGenerator::global()->bounded(tailleGrille);

        if ((row == firstClickRow && col == firstClickCol) ||
            (row >= firstClickRow - 1 && row <= firstClickRow + 1 &&
             col >= firstClickCol - 1 && col <= firstClickCol + 1)) {
            continue;
        }

        if (!mines[row][col]) {
            mines[row][col] = true;
            minesPlaced++;
        }
    }
}

void MainWindow::onCellClicked()
{
    if (!gameActive) {
        QPushButton *button = qobject_cast<QPushButton*>(sender());
        if (!button) return;

        for (int row = 0; row < tailleGrille; ++row) {
            for (int col = 0; col < tailleGrille; ++col) {
                if (cells[row][col] == button) {
                    generateMines(row, col);
                    gameActive = true;
                    timer->start(1000);
                    revealCell(row, col);
                    return;
                }
            }
        }
    }

    QPushButton *button = qobject_cast<QPushButton*>(sender());
    if (!button) return;

    for (int row = 0; row < tailleGrille; ++row) {
        for (int col = 0; col < tailleGrille; ++col) {
            if (cells[row][col] == button) {
                if (mines[row][col]) {
                    clickedMineRow = row;
                    clickedMineCol = col;
                    gameOver(false);
                    return;
                } else {
                    revealCell(row, col);
                    checkWin();
                }
                return;
            }
        }
    }
}

void MainWindow::onCellRightClicked()
{
    if (!gameActive) return;

    QPushButton *button = qobject_cast<QPushButton*>(sender());
    if (!button) return;

    for (int row = 0; row < tailleGrille; ++row) {
        for (int col = 0; col < tailleGrille; ++col) {
            if (cells[row][col] == button) {
                if (!revele[row][col]) {
                    if (drapeau[row][col]) {
                       drapeau[row][col] = false;
                        button->setIcon(QIcon(":/img/annexes_projets_demineur/poteau.png"));
                    } else if (button->icon().isNull()) {
                       drapeau[row][col] = true;
                        button->setIcon(QIcon(":/img/annexes_projets_demineur/drapeau.png"));
                        bombesRestantes--;
                    } else {
                        button->setIcon(QIcon());
                        bombesRestantes++;
                    }

                    QString bombsText = QString("%1").arg(bombesRestantes, 3, 10, QChar('0'));

                    bombsLcd->display(bombsText);
                }
                return;
            }
        }
    }
}

void MainWindow::revealCell(int row, int col)
{
    if (row < 0 || row >= tailleGrille || col < 0 || col >= tailleGrille || revele[row][col]) return;

    revele[row][col] = true;
    cells[row][col]->setEnabled(false);

    int adjacentMines = 0;
    for (int i = row - 1; i <= row + 1; ++i) {
        for (int j = col - 1; j <= col + 1; ++j) {
            if (i >= 0 && i < tailleGrille && j >= 0 && j < tailleGrille && mines[i][j]) {
                adjacentMines++;
            }
        }
    }

    if (adjacentMines > 0) {
        cells[row][col]->setText(QString::number(adjacentMines));
    } else {
        revealAdjacentCells(row, col);
    }
}

void MainWindow::revealAdjacentCells(int row, int col)
{
    for (int i = row - 1; i <= row + 1; ++i) {
        for (int j = col - 1; j <= col + 1; ++j) {
            revealCell(i, j);
        }
    }
}

void MainWindow::checkWin()
{
    for (int row = 0; row < tailleGrille; ++row) {
        for (int col = 0; col < tailleGrille; ++col) {
            if (!mines[row][col] && !revele[row][col]) {
                return;
            }
        }
    }
    gameOver(true);
}

void MainWindow::saveScore(const QString &name, int time)
{
    QString fileName;
    switch (nombreBombes) {
        case 10: fileName = "scores_easy.txt"; break;
        case 16: fileName = "scores_medium.txt"; break;
        case 64: fileName = "scores_hard.txt"; break;
        default: return;
    }

    QFile file(fileName);
    if (file.open(QIODevice::Append | QIODevice::Text)) {
        QTextStream out(&file);
        out << time << " " << name << "\n";
        file.close();
    } else {
        qWarning() << "Unable to open file to save score.";
    }
}

void MainWindow::gameOver(bool won)
{
    gameActive = false;
    timer->stop();

    if (won) {

        smileyButton->setIcon(QIcon(":/img/annexes_projets_demineur/content.png"));


        int minutes = tempsEcoule / 60;
        int seconds = tempsEcoule % 60;

        QString timeMessage;
        if (minutes > 0) {
            timeMessage = tr("You won in %1 minutes and %2 seconds!").arg(minutes).arg(seconds);
        } else {
            timeMessage = tr("You won in %1 second(s)!").arg(seconds);
        }


        bool ok;
        QString name = QInputDialog::getText(this, tr("Under what name should this score be recorded?"), timeMessage + "\n" + tr("Enter your name:"), QLineEdit::Normal, "", &ok);

        if (ok && !name.isEmpty()) {
            saveScore(name, tempsEcoule);
        }
    } else {
        smileyButton->setIcon(QIcon(":/img/annexes_projets_demineur/triste.png"));
        revelationMineEtDrapeu();
        QMessageBox::information(this, tr("Defeat"), tr("You lost!"));
    }

    showScores();
}

void MainWindow::revelationMineEtDrapeu()
{
    for (int row = 0; row < tailleGrille; ++row) {
        for (int col = 0; col < tailleGrille; ++col) {
            QPushButton *cell = cells[row][col];

            if (mines[row][col]) {
                cell->setIcon(QIcon());
                cell->setStyleSheet("background-color: white; border: 2px solid red;");

                QPixmap pixmap(30, 30);
                pixmap.fill(Qt::transparent);
                QPainter painter(&pixmap);
                painter.setRenderHint(QPainter::Antialiasing);

                if (row == clickedMineRow && col == clickedMineCol) {
                    painter.setPen(Qt::red);
                    painter.setBrush(Qt::red);
                } else {
                    painter.setPen(Qt::black);
                    painter.setBrush(Qt::black);
                }

                painter.drawEllipse(5, 5, 20, 20);
                painter.drawLine(15, 5, 15, 25);
                painter.drawLine(5, 15, 25, 15);
                painter.drawLine(5, 5, 25, 25);
                painter.drawLine(5, 25, 25, 5);

                cell->setIcon(QIcon(pixmap));
            } else if (drapeau[row][col]) {
                if (!mines[row][col]) {
                    cell->setStyleSheet("background-color: yellow; border: 2px solid red;");
                } else {
                    cell->setStyleSheet("border: 2px solid red;");
                }
            }
        }
    }
}

void MainWindow::updateTimer()
{
    tempsEcoule++;

      QString timeText = QString("%1").arg(tempsEcoule, 3, 10, QChar('0'));

      timeLcd->display(timeText);
}

void MainWindow::showScores()
{
    QStringList Options_de_difficulte = { "Easy - 10*10 with 10 bombs", "Medium - 16*16 with 16 bombs", "Hard - 32*16 with 64 bombs" };
    QString selectedDdifficulte = QInputDialog::getItem(this, tr("Choose Ddifficulte"), tr("Ddifficulte Level:"), Options_de_difficulte, 0, false);

    QString fileName;
    if (selectedDdifficulte == "Easy - 10*10 with 10 bombs") {
        fileName = "scores_easy.txt";
    } else if (selectedDdifficulte == "Medium - 16*16 with 16 bombs") {
        fileName = "scores_medium.txt";
    } else if (selectedDdifficulte == "Hard - 32*16 with 64 bombs") {
        fileName = "scores_hard.txt";
    } else {
        return;
    }

    QFile file(fileName);
    if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        QTextStream in(&file);
        QStringList scores;
        while (!in.atEnd()) {
            scores << in.readLine();
        }
        file.close();
        QMessageBox::information(this, tr("Leaderboard - ") + selectedDdifficulte, scores.join("\n"));
    } else {
        QMessageBox::warning(this, tr("Error"), tr("No score recorded for this ddifficulte."));
    }
}

void MainWindow::showAboutQt()
{
    QMessageBox::aboutQt(this, tr("About Qt"));
}

void MainWindow::showAboutMinesweeper()
{
    QMessageBox::about(this, tr("About Minesweeper"), tr("Minesweeper\n\nCreated by Guy Keny Ndayizeye"));
}
