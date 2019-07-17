package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Company;
import com.orange.oab.lmde.util.threadlocal.InitCompanyThreadLocal;

public class LMDECompanyLocalServiceImpl extends CompanyLocalServiceImpl {

    @Override
    public Company checkCompany(String webId, String mx, String shardName) throws PortalException, SystemException {
        
        Company result = null;
        InitCompanyThreadLocal.set(true);
        
        try {
            result = super.checkCompany(webId, mx, shardName);
        } catch (PortalException | SystemException e) {
            throw e;
        } finally {
            InitCompanyThreadLocal.set(false);
        }
        
        return result;
    }
    
}
