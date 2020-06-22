/*******************************************************************************
 * (c) Copyright IBM Corporation 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package wasdev.ejb.ejb.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wasdev.ejb.ejb.SampleStatelessBean;

/**
 * A servlet which injects a stateless EJB
 */
@WebServlet({"/api/v1/transaction"})
public class EJBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    SampleStatelessBean statelessBean;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        String accountNumber = request.getParameter("accountNumber");
        if (accountNumber == null || accountNumber.isEmpty()) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	writer.println("Account number cannot be empty. Please enter a valid account number");
        } else {
        	response.setStatus(HttpServletResponse.SC_OK);
        	response.setContentType("application/json");
        	String message = statelessBean.getTransactionDetails(accountNumber);
        	writer.println(message);
        }
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        String json_request_string = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (json_request_string == null || json_request_string.isEmpty()) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	writer.println("Request body cannot be empty. Please enter a valid json request as per api specification");
        } else {
        	Boolean isPersisted = statelessBean.insertTransactionDetail(json_request_string);
        	if (isPersisted) {
        		response.setStatus(HttpServletResponse.SC_OK);
            	writer.println("Transaction is successfull");
        	} else {
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            	writer.println("Transaction is not processed");
        	}
        	
        }
        
    }
}
