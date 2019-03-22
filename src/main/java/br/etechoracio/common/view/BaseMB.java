package br.com.etechoracio.common.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;

import br.com.etechoracio.common.model.BaseORM;
/**
 * Base para todos os ManagedBeans(MBs) da Aplicação.
 * 
 * <pre>
 * Last Modified  $Date: 2018/10/05 22:00:00 $
 * Last Modified by $Author: Rogério de Morais $
 * </pre>
 * 
 * @author Rogério de Morais
 * @version $Revision: 1.2 $
 */
public abstract class BaseMB {
	protected static final String RECORD_INSERT_SUCCESS = "Registro inserido com sucesso.";
	protected static final String RECORD_UPDATE_SUCCESS = "Registro alterado com sucesso.";
	protected static final String RECORD_REMOVE_SUCCESS = "Exclusão realizada com sucesso.";
	protected static final String REGISTROS_NAO_SELECIONADOS = "Não há registros selecionados para a solicitação.";
	protected static final String SK_USER_CTX = "user_context";

	protected static final String CONTEXT_PAGES = "/pages";
	public static final String PAGE_HOME = CONTEXT_PAGES + "/home.xhtml";
	public static final String PAGE_LOGIN = CONTEXT_PAGES + "/login.xhtml";

	@PostConstruct
	final void postConstructCaller() {
		postConstruct();
	}

	/**
	 * Metodo chamado após o Contrutor da Classe.
	 */
	protected void postConstruct() {
	}

	protected final FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	protected final void showInsertMessage() {
		showInfoMessage(RECORD_INSERT_SUCCESS);
	}

	protected final void showUpdateMessage() {
		showInfoMessage(RECORD_UPDATE_SUCCESS);
	}

	protected final void showDeleteMessage() {
		showInfoMessage(RECORD_REMOVE_SUCCESS);
	}

	protected final void showErrorMessage(String summary) {
		showErrorMessage(summary, false);
	}

	protected final void showErrorMessage(String identificador, String summary) {
		showErrorMessage(identificador, summary, false);
	}

	protected final void showErrorMessage(String summary, boolean useAsKey) {
		showErrorMessage(null, summary, useAsKey);
	}

	protected final void showErrorMessage(String summary, boolean useAsKey,
			Object... params) {
		showErrorMessage(null, summary, useAsKey, params);
	}

	protected final void showErrorMessage(String identificador, String summary,
			boolean useAsKey) {
		showErrorMessage(identificador, summary, useAsKey,
				new Object[] { null });
	}

	protected final void showErrorMessage(String identificador, String summary,
			boolean useAsKey, Object... params) {
		showMessage(identificador, FacesMessage.SEVERITY_ERROR, summary,
				useAsKey, params);
	}

	protected final void showInfoMessage(String summary) {
		showInfoMessage(summary, false);
	}

	protected final void showInfoMessage(String identificador, String summary) {
		showInfoMessage(identificador, summary, false);
	}

	protected final void showInfoMessage(String summary, boolean useAsKey) {
		showInfoMessage(null, summary, useAsKey);
	}

	protected final void showInfoMessage(String summary, boolean useAsKey,
			Object... params) {
		showInfoMessage(null, summary, useAsKey, params);
	}

	protected final void showInfoMessage(String identificador, String summary,
			boolean useAsKey) {
		showInfoMessage(identificador, summary, useAsKey, new Object[] { null });
	}

	protected final void showInfoMessage(String identificador, String summary,
			boolean useAsKey, Object... params) {
		showMessage(identificador, FacesMessage.SEVERITY_INFO, summary,
				useAsKey, params);
	}

	protected final void showWarnMessage(String summary) {
		showWarnMessage(summary, false);
	}

	protected final void showWarnMessage(String summary, boolean useAsKey) {
		showWarnMessage(summary, useAsKey, new Object[] { null });
	}

	protected final void showWarnMessage(String summary, boolean useAsKey,
			Object... params) {
		showMessage(null, FacesMessage.SEVERITY_WARN, summary, useAsKey, params);
	}

	protected final void showMessage(FacesMessage.Severity severity,
			String summary, boolean useAsKey) {
		showMessage(null, severity, summary, useAsKey, new Object[] { null });
	}

	protected final void showMessage(String identificador,
			FacesMessage.Severity severity, String summary, boolean useAsKey,
			Object... params) {
		if (useAsKey) {
			summary = MessageBundleLoader.getMessage(summary, params);
		}
		getFacesContext().addMessage(identificador,
				new FacesMessage(severity, summary, null));

		showMessagePostInvoke();
	}

	protected void showMessagePostInvoke() {
	}

	protected final String getMessage(String message, Object... params) {
		if (message == null) {
			return null;
		}
		if (isSelectOne(params)) {
			return String.format(message, params);
		}
		return message;
	}

	protected final void showUnselectedItemMessage(String name, String action) {
		String msg = MessageBundleLoader.getMessage(
				"abstractMB.item.nao.selecionado",
				new Object[] { name, action });
		showErrorMessage(msg);
	}

	protected final void showRequiredPropertyMessage(String fieldName) {
		String msg = MessageBundleLoader.getMessage(
				"javax.faces.component.UIInput.REQUIRED",
				new Object[] { fieldName });
		showErrorMessage(msg);
	}

	protected String getRealPath(String relPath) {
		String path = relPath;
		try {
			URL url = getFacesContext().getExternalContext().getResource(
					relPath);
			if (url != null) {
				path = url.getPath();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * <p>
	 * Adiciona um parametro de callback no {@link RequestContext}.
	 * </p>
	 * 
	 * @param name
	 *            - nome do parâmetro.
	 * @param value
	 *            - valor do parâmetro.
	 */
	protected void addCallbackParam(String name, Object value) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam(name, value);
	}

	public Date getToday() {
		return new Date();
	}

	/**
	 * Retorna o id do objeto passado.
	 * 
	 * @param <B>
	 *            - Objeto desejado
	 * @param b
	 *            - Objeto
	 * @return Identificador do objeto
	 */
	protected <B extends BaseORM> Long extrairId(B b) {
		return b == null ? null : (Long) b.getId();
	}

	/**
	 * Verifica se um dos parâmetros foi preenchido.
	 * 
	 * @param parameters
	 *            Lista de parâmetros
	 * @return
	 */
	protected boolean isSelectOne(Object... parameters) {
		for (Object object : parameters) {
			if (object != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param key
	 * @param objectToSession
	 */
	protected final void putSession(String key, Object objectToSession) {
		putSession(key, objectToSession, false);
	}

	/**
	 * @param key
	 * @param objectToSession
	 */
	protected final void putSession(String key, Object objectToSession,
			boolean create) {
		HttpServletRequest request = getRequest();
		request.getSession(create).setAttribute(key, objectToSession);
	}

	/**
	 * @return
	 */
	protected final HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext()
				.getRequest();
	}

	/**
	 * Retorna os identificadores dos grupos selecionados.
	 * 
	 * @param <B>
	 *            - Classe que implementa {@link BaseORM}
	 * @param tipo
	 *            - Vetor de {@link BaseORM}
	 * @return Lista de identificadores
	 */
	protected <B extends BaseORM> List<Long> extrairIds(B[] tipo) {
		List<Long> ids = new ArrayList<Long>();
		if (tipo.length == 0) {
			throw new RuntimeException(
					"Não há registros selecionados para a solicitação.");
		}
		for (int x = 0; x < tipo.length; x++) {
			ids.add((Long) tipo[x].getId());
		}
		return ids;
	}

	/**
	 * Retorna os identificadores dos grupos selecionados.
	 * 
	 * @param <B>
	 *            - Classe que implementa {@link BaseORM}
	 * @param tipo
	 *            - Vetor de {@link BaseORM}
	 * @return Lista de identificadores
	 */
	protected <B extends BaseORM> List<Long> extrairIds(List<B> list) {
		List<Long> ids = new ArrayList<Long>();
		if (list == null || list.isEmpty()) {
			throw new RuntimeException(
					"Não há registros selecionados para a solicitação.");
		}
		for (B b : list) {
			ids.add((Long) b.getId());
		}
		return ids;
	}

	/**
	 * Obtém um componente do Primefaces através do seu id.
	 * 
	 * @param idComponent
	 * @return
	 */
	protected UIComponent getComponent(String idComponent) {
		return FacesContext.getCurrentInstance().getViewRoot()
				.findComponent(idComponent);
	}

	/**
	 * Redefine a paginação de um {@link DataTable}.
	 * 
	 * @param idDataTable
	 *            - Identificador do {@link DataTable}
	 */
	protected void resetPagination(String idDataTable) {
		DataTable dataTable = (DataTable) getComponent(idDataTable);
		dataTable.setFirst(0);
	}

	/**
	 * Define a aba que será exibida no componente.
	 * 
	 * @param index
	 */
	protected void setActiveIndexTab(int index, String idTab) {
		TabView tab = (TabView) getComponent(idTab);
		tab.setActiveIndex(index);
	}

	protected void setActiveIndexTab(String idTab) {
		setActiveIndexTab(0, idTab);
	}
	
	/**
	 * <p>
	 * Adiciona um parametro de callback no {@link RequestContext}.
	 * </p>
	 * 
	 * @param name
	 *            - nome do parâmetro.
	 * @param value
	 *            - valor do parâmetro.
	 */
	protected void addCallBackParam(String name, Object value) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam(name, value);
	}
	
	/**
     * 
     */
	protected void validationFailed() {
		addCallBackParam("validationFailed", true);
	}
	
	protected void forward(String outcome) {
		performNavigation(outcome, false);
	}

	protected void redirect(String outcome) {
		performNavigation(outcome, true);
	}
	
	private void performNavigation (String outcome, boolean facesRedirect, Object... parameters){
		String redirect = facesRedirect ? "?faces-redirect=true" : "";
		if (null != parameters && parameters.length > 0) {
			StringBuilder sb = new StringBuilder(redirect);
			sb.append("&includeViewParams=true&");
			for (int i = 0; i < parameters.length; i += 2) {
				sb.append(parameters[i].toString());
				sb.append("=");
				sb.append(parameters[i + 1]);
				sb.append("&");
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			redirect = sb.toString();
		}
		
		ConfigurableNavigationHandler navigateHandler = (ConfigurableNavigationHandler)
				getFacesContext().getApplication().getNavigationHandler();
		navigateHandler.performNavigation(outcome + redirect);
	}
	
	protected <V> List<V> buildEnumValues(V[] values) {
		return Arrays.asList(values);
	}
}
