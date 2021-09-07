package org.sakaiproject.jsf.help;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;


public class FaceRecognitionSetRender extends Renderer
{
    //private static String DEFAULT_WELCOME_PAGE = "html/help.html";
    private static String SIGNUP_FACE_HTML= "html/openWebCam.html";
    private static  String LOGIN_FACE_HTML ="html/openWebCamLogin.html";

    /**
     * supports component type
     * @param component
     * @return true if supporte
     */
    public boolean supportsComponentType(UIComponent component)
    {
        return (component instanceof UIData);
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        String faceURL = (String) component.getAttributes().get(
                "faceURL");

        String welcomepage = SIGNUP_FACE_HTML;
        if(faceURL!=null && faceURL.equalsIgnoreCase("LoginFaceRecognition")){
            welcomepage= LOGIN_FACE_HTML;
        }
        //extra 2 linii de la anto:

        writer.write("<!DOCTYPE html>\n");
        writer.write("<html><head><title>" + "Face Recognition" + "</title></head>\n");
        writer.write("<iframe  width= \"100%\" src=\"" + welcomepage + "\" name=\"content\" title=\"Help Content\" style=\"position: absolute; height: 100%; border: none\" scrolling=\"yes\" >");
        writer.write("</iframe>");

        writer.write("</html>");


    }
}
