#pragma once
#include <QMainWindow>
#include <QGridLayout>
#include <QLCDNumber>
#include <QPushButton>
#include <QTimer>
#include <QVector>
#include <QFile>
#include <QTextStream>
#include <QMessageBox>
#include <QInputDialog>

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void newGame();
    void onCellClicked();
    void onCellRightClicked();
    void updateTimer();
    void showScores();
    void showAboutQt();
    void showAboutMinesweeper();

private:
    void setupMenu();
    void setupGameBoard();
    void generateMines(int firstClickRow, int firstClickCol);
    void revealCell(int row, int col);
    void revealAdjacentCells(int row, int col);
    void checkWin();
    void saveScore(const QString &name, int time);
    void revelationMineEtDrapeu();
    void gameOver(bool won);

    QGridLayout *gridLayout;
    QVector<QVector<QPushButton*>> cells;
    QVector<QVector<bool>> mines;
    QVector<QVector<bool>> revele;
    QVector<QVector<bool>>drapeau;
    QLCDNumber *timeLcd;
    QLCDNumber *bombsLcd;
    QPushButton *smileyButton;
    QTimer *timer;
    int tempsEcoule;
    int bombesRestantes;
    int tailleGrille;
    int nombreBombes;
    QString currentDifficulty;
    bool gameActive;
    int clickedMineRow;
    int clickedMineCol;
};
