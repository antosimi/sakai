package org.sakaiproject.tool.help;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.opencv.imgcodecs.Imgcodecs;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.user.impl.DBFaceRecognitionService;
import sun.misc.BASE64Decoder;
import sun.reflect.Reflection;
import org.opencv.face.EigenFaceRecognizer;



@Slf4j
public class FaceRecognitionServlet extends HttpServlet{
    private DBFaceRecognitionService dbFaceRecognitionService;
    private String emailParam=null;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        nu.pattern.OpenCV.loadShared();
        Loader.load(opencv_java.class);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPost(req, resp);
        String ph = readParam(req);
        Mat src = faceDetetction(ph);
        dbFaceRecognitionService=new DBFaceRecognitionService();
        //dbFaceRecognitionService.insertValues("admin",photo);
        //dbFaceRecognitionService.readValues();

       // readParam(req);
        faceMatching(dbFaceRecognitionService,src);
        //int nr_faces = faceDetetction();
//
//        PrintWriter out = resp.getWriter();
//        out.println("Buna Anto raspunsul la adunare este = " + nr_faces);

//        int a= Integer.parseInt(req.getParameter("num1"));
//        int b= Integer.parseInt(req.getParameter("num2"));
//        int c=a+b;
//
//        PrintWriter out = resp.getWriter();
//        out.println("Buna Anto raspunsul la adunare este = " + c);
    }

    private Mat  faceDetetction(String photoMatch) {
        // Loading the OpenCV core library
        //System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
       // System.load( "/opencv-452.jar" );
       // nu.pattern.OpenCV.loadShared(); //add this


            // Reading the Image from the file and storing it in to a Matrix object
            //String file = "/Users/antoniasimionescu/Desktop/face_det/faces_for_db/anto1.png";
            String file = "/Users/antoniasimionescu/IdeaProjects/sakai_final_destination/help/help-tool/src/resources/anto1.png";
            Mat src = Imgcodecs.imread(file);
           // Mat src = base642Mat(photoMatch);
            Mat  image_roi=null;

            //varianta mai ok sa ia poza direct din param, sa nu mai salveze local
            //Mat src = base642Mat(photoBase64);

            // Instantiating the CascadeClassifier
            String xmlFile = "/Users/antoniasimionescu/IdeaProjects/sakai_final_destination/help/help-tool/src/lbpcascades/lbpcascade_frontalface.xml";
            CascadeClassifier classifier = new CascadeClassifier(xmlFile);

            // Detecting the face in the snap
            MatOfRect faceDetections = new MatOfRect();
            classifier.detectMultiScale(src, faceDetections);

            Rect rectCrop = null;
            // Drawing boxes
            for (Rect rect : faceDetections.toArray()) {
                Imgproc.rectangle(
                        src,                                               // where to draw the box
                        new Point(rect.x, rect.y),                            // bottom left
                        new Point(rect.x + rect.width, rect.y + rect.height), // top right
                        new Scalar(0, 0, 255),
                        3                                                     // RGB colour
                );
                //Cropping the image:
                //rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
                rectCrop = new Rect(
                        new Point(rect.x, rect.y),                            // bottom left
                        new Point(rect.x + rect.width, rect.y + rect.height) // top right
                );
                Size scaleSize = new Size(300,300);
                Mat image_intermediar= new Mat(src, rectCrop);
                image_roi=new Mat();
                Imgproc.resize( image_intermediar, image_roi, scaleSize);
                // Writing the image
                Imgcodecs.imwrite("/Users/antoniasimionescu/Desktop/face_det/faces_cropped/cacat25.jpg", image_roi);
                Imgproc.cvtColor(image_roi, image_roi, Imgproc.COLOR_BGR2GRAY);


            }


        // Writing the image
        //Imgcodecs.imwrite("/Users/antoniasimionescu/Desktop/face_det/cacat1.jpg", src);

        System.out.println("Image Processed");
        return image_roi;
    }

    public String readParam(HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            String streamLength = req.getHeader("Content-Length");
            int streamIntLength = Integer.parseInt(streamLength);
            InputStream inputStream = req.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream));
                char[] charBuffer = new char[streamIntLength];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        }
        emailParam = stringBuilder.toString().split(" ##### ")[0];
        String body = stringBuilder.toString().split(" ##### ")[1];
        savePhotoLocally(body);

        return body;
    }

    public void savePhotoLocally(String base64String) throws IOException {
        byte[] decoded = Base64.decodeBase64(base64String);
        String path = "/Users/antoniasimionescu/IdeaProjects/sakai_final_destination/help/help-tool/src/resources/anto1.png";
        OutputStream out1 = null;
        try {
            out1 = new BufferedOutputStream(new FileOutputStream(path));
            out1.write(decoded);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out1 != null) {
             out1.close();
            }
        }
    }

    public void faceMatching(DBFaceRecognitionService dbFaceRecognitionService, Mat photoTest){
        HashMap<Integer,String> dbFaces=dbFaceRecognitionService.readValuesByEmail(emailParam);
        ArrayList<Mat> photosMat= new ArrayList<>();
        for (String p : dbFaces.values()) {
            photosMat.add(base642Mat(p));
        }

        //ArrayList<Mat> images=new ArrayList<>();
        //ArrayList<Integer> labels=new ArrayList<>();


            Mat matPhotoSrc = photoTest;
            MatOfInt labelsMat=new MatOfInt();
            labelsMat.fromList(new ArrayList<>(dbFaces.keySet()));
           // EigenFaceRecognizer efr=EigenFaceRecognizer.create();
            LBPHFaceRecognizer lbph=LBPHFaceRecognizer.create();
            lbph.train(photosMat, labelsMat);
            int[] outLabel=new int[1];
            double[] outConf=new double[1];
            lbph.predict(matPhotoSrc,outLabel,outConf);

            System.out.println("***Predicted label is "+outLabel[0]+".***");
            System.out.println("***Confidence value is "+outConf[0]+".***");


    }

    /**
     * base64 to Mat
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public  Mat base642Mat(String base64) {
//        // Decode base64
//        Mat matImage = null;
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] origin = new byte[0];
//
//        try {
//            origin = decoder.decodeBuffer(base64);
//            InputStream in = new ByteArrayInputStream(origin); // use b as the input stream;
//            BufferedImage image = ImageIO.read(in);
//            matImage = BufImg2Mat(image, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);// CvType.CV_8UC3
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return matImage;

        Mat matImage = null;
        Mat src = null;
        byte[] imgbytes = DatatypeConverter.parseBase64Binary(base64);

         src = Imgcodecs.imdecode(new MatOfByte(imgbytes), Imgcodecs.IMREAD_UNCHANGED);
         Size scaleSize = new Size(300,300);
         matImage= new Mat();
         Imgproc.resize( src, matImage, scaleSize);
         Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_BGR2GRAY);
         return matImage;
    }

    /**
     * Convert BufferedImage to Mat
     *
     * @param original BufferedImage to be converted
     * @param imgType The type of bufferedImage such as BufferedImage.TYPE_3BYTE_BGR
     * @param matType is converted to mat type such as CvType.CV_8UC3
     */
    public  Mat BufImg2Mat(BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType) {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

}
