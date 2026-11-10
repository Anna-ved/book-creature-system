package by.rozze.bookcreaturesystem.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class SessionBean {

    public String logout() throws ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        request.logout();
        context.invalidateSession();

        return "/index.xhtml?faces-redirect=true";
    }
}