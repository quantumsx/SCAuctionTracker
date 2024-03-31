import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class MouseKeyboard {


    public void pressButton(String buttonName) throws AWTException, InterruptedException {
        Robot robot = new Robot();

        int delay = getRandomDelayBeforeAction();
        robot.delay(delay);

        switch (buttonName) {
            case "P" -> {
                robot.keyPress(KeyEvent.VK_P);
                Thread.sleep(20);
                robot.keyRelease(KeyEvent.VK_P);
            }
            case "CTRLV" -> {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);

                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }

            case "ALTENTER" -> {
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_ENTER);

                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }

        }

        System.out.println("Нажал на: " + buttonName);


    }
    public void clickAt(String areaForClickName) throws AWTException, InterruptedException {
        Robot robot = new Robot();

        int delay = getRandomDelayBeforeAction();
        robot.delay(delay);

        int[] positionForClick = chooseAreaForClick(areaForClickName);

        robot.mouseMove(positionForClick[0], positionForClick[1]);

        Thread.sleep(50);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


        System.out.println("Нажал на: " + areaForClickName +" | "+ "Координаты: X= " + positionForClick[0] + " Y= " + positionForClick[1] + " (" + delay + ")");
    }

    public int getRandomDelayBeforeAction(){
        Random random = new Random();
        int delay = random.nextInt(150,180);
        return delay;
    }

    public int[] getRandomPositionForClick(int x0, int y0, int x1, int y1){
        Random random = new Random();
        int[] randomPosition = new int[2];

        randomPosition[0] = random.nextInt(x0, x1);
        randomPosition[1] = random.nextInt(y0, y1) + ScanImage.itemInListNumber * 37;
        return randomPosition;
    }

    public int[] chooseAreaForClick(String areaName){
        switch (areaName) {
            case "Ввод предмета" -> {
                int insertItemAreaX0 = 1180;
                int insertItemAreaY0 = 340;

                int insertItemAreaX1 = 1290;
                int insertItemAreaY1 = 348;

                return getRandomPositionForClick(insertItemAreaX0,insertItemAreaY0,insertItemAreaX1,insertItemAreaY1);
            }
            case "Аукцион" -> {
                int auctionButtonAreaX0 = 519;
                int auctionButtonAreaY0 = 544;

                int auctionButtonAreaX1 = 627;
                int auctionButtonAreaY1 = 557;

                return getRandomPositionForClick(auctionButtonAreaX0,auctionButtonAreaY0,auctionButtonAreaX1, auctionButtonAreaY1);
            }
            case "Поиск" -> {
                int findButtonAreaX0 = 1309;
                int findButtonAreaY0 = 338;

                int findButtonAreaX1 = 1357;
                int findButtonAreaY1 = 346;

                return getRandomPositionForClick(findButtonAreaX0,findButtonAreaY0,findButtonAreaX1,findButtonAreaY1);
            }
            case "Сортировка" -> {
                int sortButtonAreaX0 = 1270;
                int sortButtonAreaY0 = 378;

                int sortButtonAreaX1 = 1340;
                int sortButtonAreaY1 = 382;

                return getRandomPositionForClick(sortButtonAreaX0,sortButtonAreaY0,sortButtonAreaX1,sortButtonAreaY1);
            }
            case "Предмет" -> {
                int firstItemButtonAreaX0 = 930;
                int firstItemButtonAreaY0 = 395;

                int firstItemButtonAreaX1 = 1380;
                int firstItemButtonAreaY1 = 425;

                return getRandomPositionForClick(firstItemButtonAreaX0,firstItemButtonAreaY0,firstItemButtonAreaX1,firstItemButtonAreaY1);
            }

            case "Купить" -> {
                int buyButtonAreaX0 = 1280;
                int buyButtonAreaY0 = 437;

                int buyButtonAreaX1 = 1356;
                int buyButtonAreaY1 = 452;

                return getRandomPositionForClick(buyButtonAreaX0,buyButtonAreaY0,buyButtonAreaX1,buyButtonAreaY1);
            }

            case "Закрыть" -> {
                int closeNotificationAreaX0 = 880;
                int closeNotificationAreaY0 = 570;

                int closeNotificationAreaX1 = 1040;
                int closeNotificationAreaY1 = 600;

                return getRandomPositionForClick(closeNotificationAreaX0,closeNotificationAreaY0,closeNotificationAreaX1,closeNotificationAreaY1);
            }
        }
        return new int[0];
    }
}
