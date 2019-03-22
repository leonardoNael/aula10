package br.com.etechoracio.common.view;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * <p>
 * Utility class which loads the applications message resource bundle. This class can be used to internationalize string
 * values that are located in Beans.
 * </p>
 * <p>
 * This bean can be access statically or setup as a Application scoped bean in the faces_config.xml
 * </p>
 * <p>
 * This Bean should be scoped in JSF as an Application Bean. And as a result can be passed into other beans via chaining
 * or by loading via the FacesContext. Loading an Application scoped bean "messageLoader" via the FacesContext would
 * look like the following:
 * </p>
 * <p>
 * JSF 1.1:
 * </p>
 * 
 * <pre>
 * Application application = FacesContext.getCurrentInstance().getApplication();
 * MessageBundleLoader messageLoader = ((MessageBundleLoader) application.createValueBinding(&quot;#{messageLoader}&quot;).getValue(
 *         FacesContext.getCurrentInstance()));
 * </pre>
 * <p>
 * JSF 1.2:
 * </p>
 * 
 * <pre>
 * FacesContext fc = FacesContext.getCurrentInstance();
 * ELContext elc = fc.getELContext();
 * ExpressionFactory ef = fc.getApplication().getExpressionFactory();
 * ValueExpression ve = ef.createValueExpression(elc, expr, Object.class);
 * </pre>
 * 
 * @since 1.7
 */
public class MessageBundleLoader {

    public static final String MESSAGE_PATH = "resources.messages";

    private static HashMap<String, ResourceBundle> messageBundles = new HashMap<String, ResourceBundle>();

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     * 
     * @param key
     *            the key for the desired string
     * @return the string for the given key. If the key string value is not found the key itself is returned.
     */
    public static String getMessage(String key) {
        return getMessage(key, new Object[] {});
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     * 
     * @param key
     *            the key for the desired string
     * @param params
     *            message parameters.
     * @return the string for the given key. If the key string value is not found the key itself is returned.
     */
    public static String getMessage(String key, Object... params) {
        if (key == null) {
            return null;
        }
        try {
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (locale == null) {
                locale = new Locale("pt", "BR");
            }
            ResourceBundle messages = (ResourceBundle) messageBundles.get(locale.toString());
            if (messages == null) {
                messages = ResourceBundle.getBundle(MESSAGE_PATH, locale);
                messageBundles.put(locale.toString(), messages);
            }
            return MessageFormat.format(messages.getString(key), params);
        }
        // on any failure we just return the key, which should aid in debugging.
        catch (Exception e) {
            return key;
        }
    }
}
