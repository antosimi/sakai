package org.sakaiproject.jsf.help;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;

public class FaceRecognitionSetTag extends UIComponentTag
{
    private String helpWindowTitle;

    private String faceURL;

    /**
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        return "javax.faces.Data";
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        return "FaceRecognitionSet";
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        FacesContext context = getFacesContext();

        if (helpWindowTitle != null)
        {
            if (isValueReference(helpWindowTitle))
            {
                component.setValueBinding("helpWindowTitle", context.getApplication()
                        .createValueBinding(helpWindowTitle));
            }
            else
            {
                component.getAttributes().put("helpWindowTitle", helpWindowTitle);
            }
        }
        if (faceURL != null)
        {
            if (isValueReference(faceURL))
            {
                component.setValueBinding("faceURL", context.getApplication()
                        .createValueBinding(faceURL));
            }
            else
            {
                component.getAttributes().put("faceURL", faceURL);
            }
        }
    }



    /**
     * get help window title
     * @return Returns the helpWindowTitle.
     */
    public String getHelpWindowTitle()
    {
        return helpWindowTitle;
    }

    /**
     * set help window title
     * @param helpWindowTitle The helpWindowTitle to set.
     */
    public void setHelpWindowTitle(String helpWindowTitle)
    {
        this.helpWindowTitle = helpWindowTitle;
    }

    /**
     * get help URL
     * @return help URL
     */
    public String getFaceURL()
    {
        return faceURL;
    }

    /**
     * set help URL
     * @param faceURL
     */
    public void setFaceURL(String faceURL)
    {
        this.faceURL = faceURL;
    }
}