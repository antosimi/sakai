package org.sakaiproject.tool.help;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_java;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.sakaiproject.component.cover.ComponentManager;

import org.sakaiproject.login.api.LoginCredentials;
import org.sakaiproject.login.api.LoginService;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.impl.DBFaceRecognitionService;
import org.sakaiproject.user.impl.DbUserService;

import javax.security.auth.login.LoginException;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FaceRecognitionLoginServlet extends HttpServlet {
    private DBFaceRecognitionService dbFaceRecognitionService;
    private String emailParam=null;
    private boolean emailExists;


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        nu.pattern.OpenCV.loadShared();
        Loader.load(opencv_java.class);
    }



    protected void  meh(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, LoginException {
        // Get the Sakai session
        //getCachedUserByEid()
        dbFaceRecognitionService=new DBFaceRecognitionService();
        Session session = SessionManager.getCurrentSession();
        Map<Integer,String> userValList = dbFaceRecognitionService.getUserFromEmail("24a16787-9b37-4d7d-9393-1bfb67301c16");
        LoginCredentials credentials = new LoginCredentials(userValList.get(1),userValList.get(2),"0000");
        credentials.setSessionId(session.getId());

        // get my tool registration
        Tool tool = (Tool) req.getAttribute(Tool.TOOL);
        LoginService loginService = (LoginService) ComponentManager.get(LoginService.class);

        loginService.authenticate(credentials);
        String returnUrl = (String) session.getAttribute(Tool.HELPER_DONE_URL);
        //complete(returnUrl, session, tool, res);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //meh(req,resp);
        dbFaceRecognitionService=new DBFaceRecognitionService();
        String ph = readParam(req);
        if(emailExists==false || emailParam ==null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "This email does not exists.");
            return;
        }
        Mat src = faceDetetction(ph);

        boolean result = faceMatching(dbFaceRecognitionService,src);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println(result);
            out.close();
        }

    }

    private Mat  faceDetetction(String photoMatch) {

       // String file = "/Users/antoniasimionescu/IdeaProjects/sakai_final_destination/help/help-tool/src/resources/anto1.png";
        //Mat src = Imgcodecs.imread(file);
         Mat src = base642Mat(photoMatch);
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
            //Imgcodecs.imwrite("/Users/antoniasimionescu/Desktop/face_det/faces_cropped/cacat25.jpg", image_roi);

        }

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
        emailExists= checkEmail(emailParam);
        String body = stringBuilder.toString().split(" ##### ")[1];
        savePhotoLocally(body);

        return body;
    }

    public void savePhotoLocally(String base64String) throws IOException {
        byte[] decoded = Base64.decodeBase64(base64String);
        String path = "/Users/antoniasimionescu/IdeaProjects/sakai_final_destination/help/help-tool/src/resources/anto2.png";
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

    public boolean faceMatching(DBFaceRecognitionService dbFaceRecognitionService, Mat photoTest){
        HashMap<Integer,String> dbFaces=dbFaceRecognitionService.readValuesByEmail(emailParam);
        ArrayList<Mat> photosMat= new ArrayList<>();
        for (String p : dbFaces.values()) {
            photosMat.add(base642Mat(p));
        }

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

        return (outConf[0]<=60);

    }

    /**
     * base64 to Mat
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public  Mat base642Mat(String base64) {

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

    private boolean checkEmail(String email){
        return dbFaceRecognitionService.checkEmailExists(email);
    }


}
