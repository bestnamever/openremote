/*
 * Copyright 2017, OpenRemote Inc.
 *
 * See the CONTRIBUTORS.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.util.*;

import static org.openremote.model.Constants.MASTER_REALM;
import static org.openremote.model.Constants.RESTRICTED_USER_REALM_ROLE;

/**
 * This can be used (among other things) to query the REALM table in JPA queries.
 */
@Entity
@Subselect("select * from PUBLIC.REALM") // Map this immutable to an SQL view, don't use/create table
public class Tenant {

    protected static Field[] propertyFields;

    @Id
    protected String id;

    @Column(name = "NAME")
    protected String realm;

    @Formula("(select ra.VALUE from PUBLIC.REALM_ATTRIBUTE ra where ra.REALM_ID = ID and ra.name = 'displayName')")
    protected String displayName;

    @Column(name = "ENABLED")
    protected Boolean enabled;

    @Column(name = "NOT_BEFORE")
    protected Double notBefore; // This will explode in 2038

    @Column(name = "reset_password_allowed")
    protected Boolean resetPasswordAllowed;

    @Column(name = "duplicate_emails_allowed")
    protected Boolean duplicateEmailsAllowed;

    @Column(name = "remember_me")
    protected Boolean rememberMe;

    @Column(name = "registration_allowed")
    protected Boolean registrationAllowed;

    @Column(name = "reg_email_as_username")
    protected Boolean registrationEmailAsUsername;

    @Column(name = "verify_email")
    protected Boolean verifyEmail;

    @Column(name = "login_with_email_allowed")
    protected Boolean loginWithEmail;

    @Column(name = "login_theme")
    protected String loginTheme;

    @Column(name = "account_theme")
    protected String accountTheme;

    @Column(name = "admin_theme")
    protected String adminTheme;

    @Column(name = "email_theme")
    protected String emailTheme;

    @Column(name = "access_token_lifespan")
    protected Integer accessTokenLifespan;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "REALM_ID")
    protected Set<RealmRole> realmRoles;

    public Tenant() {
    }

    public Tenant(String id, String realm, String displayName, Boolean enabled) {
        this.id = id;
        this.realm = realm;
        this.displayName = displayName;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "{Tenant.realm.NotNull}")
    @Size(min = 3, max = 255, message = "{Tenant.realm.Size}")
    @Pattern(regexp = "[a-zA-Z0-9\\-_]+", message = "{Tenant.realm.Pattern}")
    public String getRealm() {
        return realm;
    }

    public Tenant setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    @NotNull(message = "{Tenant.displayName.NotNull}")
    @Size(min = 3, max = 255, message = "{Tenant.displayName.Size}")
    public String getDisplayName() {
        return displayName;
    }

    public Tenant setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Tenant setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Double getNotBefore() {
        return notBefore;
    }

    public Tenant setNotBefore(Double notBefore) {
        this.notBefore = notBefore;
        return this;
    }

    public Boolean getResetPasswordAllowed() {
        // We allow password reset by default
        return resetPasswordAllowed != null ? resetPasswordAllowed : true;
    }

    public Tenant setResetPasswordAllowed(Boolean resetPasswordAllowed) {
        this.resetPasswordAllowed = resetPasswordAllowed;
        return this;
    }

    public Boolean getDuplicateEmailsAllowed() {
        return duplicateEmailsAllowed != null && duplicateEmailsAllowed;
    }

    public Tenant setDuplicateEmailsAllowed(Boolean duplicateEmailsAllowed) {
        this.duplicateEmailsAllowed = duplicateEmailsAllowed;
        return this;
    }

    public Boolean getVerifyEmail() {
        return verifyEmail != null && verifyEmail;
    }

    public Tenant setVerifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
        return this;
    }

    public Boolean getLoginWithEmail() {
        return loginWithEmail != null && loginWithEmail;
    }

    public Tenant setLoginWithEmail(Boolean loginWithEmail) {
        this.loginWithEmail = loginWithEmail;
        return this;
    }

    public Integer getAccessTokenLifespan() {
        return accessTokenLifespan;
    }

    public Tenant setAccessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
        return this;
    }

    public Boolean getRememberMe() {
        return rememberMe != null && rememberMe;
    }

    public Tenant setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }

    public Boolean getRegistrationAllowed() {
        return registrationAllowed != null && registrationAllowed;
    }

    public Tenant setRegistrationAllowed(Boolean registrationAllowed) {
        this.registrationAllowed = registrationAllowed;
        return this;
    }

    public Boolean getRegistrationEmailAsUsername() {
        return registrationEmailAsUsername != null && registrationEmailAsUsername;
    }

    public Tenant setRegistrationEmailAsUsername(Boolean registrationEmailAsUsername) {
        this.registrationEmailAsUsername = registrationEmailAsUsername;
        return this;
    }

    public boolean isActive(double currentTimeMillis) {
        return enabled != null && enabled
            && (notBefore == null || notBefore == 0 || notBefore <= (currentTimeMillis/1000));
    }

    public String getLoginTheme() {
        return loginTheme;
    }

    public Tenant setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
        return this;
    }

    public String getAccountTheme() {
        return accountTheme;
    }

    public Tenant setAccountTheme(String accountTheme) {
        this.accountTheme = accountTheme;
        return this;
    }

    public String getAdminTheme() {
        return adminTheme;
    }

    public Tenant setAdminTheme(String adminTheme) {
        this.adminTheme = adminTheme;
        return this;
    }

    public String getEmailTheme() {
        return emailTheme;
    }

    public Tenant setEmailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
        return this;
    }

    public Set<RealmRole> getRealmRoles() {
        return realmRoles;
    }

    @JsonIgnore
    public Set<RealmRole> getNormalisedRealmRoles() {
        Set<RealmRole> tempSet = new LinkedHashSet<>(getDefaultRealmRoles(getRealm()));
        if (realmRoles != null) {
            tempSet.addAll(realmRoles);
        }
        return tempSet;
    }

    public Tenant setRealmRoles(Set<RealmRole> realmRoles) {
        this.realmRoles = realmRoles;
        return this;
    }

    public static Field[] getPropertyFields() {
        if (propertyFields == null) {
            propertyFields = Arrays.stream(Tenant.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class) || field.isAnnotationPresent(Formula.class))
                .toArray(Field[]::new);
        }
        return propertyFields;
    }

    protected static List<RealmRole> getDefaultRealmRoles(String realm) {
        if (MASTER_REALM.equals(realm)) {
            return Arrays.asList(
                new RealmRole("default-roles-master"),
                new RealmRole("admin"),
                new RealmRole("create-realm"),
                new RealmRole("offline_access"),
                new RealmRole("uma_authorization"),
                new RealmRole(RESTRICTED_USER_REALM_ROLE, "Restricted access to assets")
            );
        }

        return Arrays.asList(
            new RealmRole("default-roles-" + realm),
            new RealmRole("offline_access"),
            new RealmRole("uma_authorization"),
            new RealmRole(RESTRICTED_USER_REALM_ROLE, "Restricted access to assets")
        );
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + id + '\'' +
            ", realm='" + realm + '\'' +
            ", displayName='" + displayName + '\'' +
            ", enabled=" + enabled +
            ", notBefore=" + notBefore +
            ", resetPasswordAllowed=" + resetPasswordAllowed +
            ", duplicateEmailsAllowed=" + duplicateEmailsAllowed +
            ", rememberMe=" + rememberMe +
            ", registrationAllowed=" + registrationAllowed +
            ", registrationEmailAsUsername=" + registrationEmailAsUsername +
            ", verifyEmail=" + verifyEmail +
            ", loginWithEmail=" + loginWithEmail +
            ", loginTheme='" + loginTheme + '\'' +
            ", accountTheme='" + accountTheme + '\'' +
            ", adminTheme='" + adminTheme + '\'' +
            ", emailTheme='" + emailTheme + '\'' +
            ", accessTokenLifespan=" + accessTokenLifespan +
            ", realmRoles=" + realmRoles +
            '}';
    }
}
