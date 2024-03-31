import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
public class ScanImage {


        public static final int WM_SETTEXT = 0x000C;
        public static int itemInListNumber = 0;

        public static void pressButtons(String itemName, String foundItemPrice) throws AWTException, TesseractException, IOException, InterruptedException {

            long startTime = System.currentTimeMillis();

            String test = "STALCRAFT";
            getProcessWindow(test);

            StringSelection selection = new StringSelection(itemName);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);

            MouseKeyboard mouseKeyboard = new MouseKeyboard();

            itemInListNumber = 0;
            mouseKeyboard.pressButton("ALTENTER");
            mouseKeyboard.pressButton("ALTENTER");
            mouseKeyboard.pressButton("P");
            mouseKeyboard.clickAt("Аукцион");
            mouseKeyboard.clickAt("Ввод предмета");
            mouseKeyboard.pressButton("CTRLV");
            mouseKeyboard.clickAt("Поиск");
            mouseKeyboard.clickAt("Сортировка");
            Thread.sleep(100);
            mouseKeyboard.clickAt("Сортировка");
            Thread.sleep(500);

            boolean foundFirstPrice = false;


            while (!foundFirstPrice) {
                String itemOnAuctionPrice = imgProcessing(itemInListNumber);

                if (!itemOnAuctionPrice.equals("Цена не определилась")) {

                    if (itemOnAuctionPrice.equals(foundItemPrice)) {

                        mouseKeyboard.clickAt("Предмет");
                        mouseKeyboard.clickAt("Купить");
                        mouseKeyboard.pressButton("P");
                        System.out.println("Предмет успешно приобретён");
                        foundFirstPrice = true;
                    } else {
                        System.out.println("Предмет уже выкупили");
                        mouseKeyboard.pressButton("P");
                        break;
                    }
                } else {

                    itemInListNumber++;
                    if (itemInListNumber == 10) {
                        mouseKeyboard.pressButton("P");
                        System.out.println("Не удалось приобрести предмет. Все предметы без цены");
                        break;
                    }
                }

            }

            long endTime = System.currentTimeMillis();
            long executionTime = (endTime - startTime);
            System.out.println("Время выполнения кода: " + executionTime + " секунд");

        }




        public static String imgProcessing(int itemNumber) throws IOException, AWTException, TesseractException {

            BufferedImage ipimage = getScreenshot(itemNumber);

            BufferedImage resizedImage = resize(ipimage,4,itemNumber);

            // getting RGB content of the whole image file
            double d
                    = resizedImage
                    .getRGB(resizedImage.getTileWidth() / 2,
                            resizedImage.getTileHeight() / 2);

            System.out.println(d);

            // comparing the values
            // and setting new scaling values
            // that are later on used by RescaleOP
            if (d >= -1.9592427E7 && d < -7254228) {
                return processImg(resizedImage, 1.6f, -60f);
            }
            else if (d >= -7254228 && d < -2171170) {
                return processImg(resizedImage, 1.4f, -30f);
            }
            else if (d >= -2171170 && d < -1907998) {
                return processImg(resizedImage, 1.4f, -30f);
            }
            else if (d >= -1907998 && d < -257) {
                return processImg(resizedImage, 1.4f, -30f);
            }
            else if (d >= -257 && d < -1) {
                return processImg(resizedImage, 1.1f, -20f);
            }
            else if (d >= -1 && d < 2) {
                return processImg(resizedImage, 1.1f, -30f);
            }
            return "d вне диапазона";
        }

        public static String correctPrice(String price){
            String newPrice = price.replaceAll("\\s","");

            int index = newPrice.indexOf('p');
            if (index != -1) {
                return newPrice.substring(0,index);
            }
            else {
                return "Цена не определилась";
            }
        }

        public static String processImg(BufferedImage ipimage,
                                        float scaleFactor,
                                        float offset) throws IOException, TesseractException {

            BufferedImage opimage = new BufferedImage(540, 120, ipimage.getType());


            Graphics2D graphic = opimage.createGraphics();


            graphic.drawImage(ipimage, 0, 0, 540, 120, null);
            graphic.dispose();


            RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);


            BufferedImage fopimage = rescale.filter(opimage, null);
            File outputFile = new File("C:\\Users\\Artem\\Desktop\\testimage\\output" + itemInListNumber + ".png");
            ImageIO.write(fopimage, "png", outputFile);


            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Users\\Artem\\Desktop\\tessdata");
            tesseract.setTessVariable("user_defined_dpi", "600");


            String str = tesseract.doOCR(fopimage);
            String correctedString = correctPrice(str);

            System.out.println("Исходный результат OCR: " + str);
            System.out.println("Исправленный результат OCR: " + correctedString);

            return (correctedString);
        }

        public static void getProcessWindow(String windowTitleToFind) {

            WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, windowTitleToFind);

            User32.INSTANCE.ShowWindow(hwnd, User32.SW_RESTORE);
            User32.INSTANCE.SetForegroundWindow(hwnd);
        }


        public static BufferedImage getScreenshot(int itemNumber) throws AWTException, IOException {
            Robot robot = new Robot();
            int x = 1248;
            int y = 395 + itemNumber * 37;

            Rectangle area = new Rectangle(x, y, 135, 30);
            BufferedImage screenshotFake = robot.createScreenCapture(area);
            BufferedImage screenshot = robot.createScreenCapture(area);

            File file = new File("C:\\Users\\Artem\\Desktop\\testimage\\outputtest" + itemNumber + ".png");
            ImageIO.write(screenshot, "png", file);

            return screenshot;
        }

        public static BufferedImage resize(BufferedImage img, double scale,int itemNumber) {
            int newWidth = (int) (img.getWidth() * scale);
            int newHeight = (int) (img.getHeight() * scale);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, img.getType());
            resizedImage.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null);


            File outputFile = new File("C:\\Users\\Artem\\Desktop\\testimage\\outputtest_resized" + itemNumber + ".png");
            try {
                ImageIO.write(resizedImage, "png", outputFile);
                System.out.println("Изображение успешно сохранено.");
            } catch (IOException e) {
                System.out.println("Ошибка при сохранении изображения: " + e.getMessage());
            }

            return resizedImage;
        }

}
