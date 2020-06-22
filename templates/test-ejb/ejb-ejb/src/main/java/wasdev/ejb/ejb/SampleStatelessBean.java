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
package wasdev.ejb.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.poc.cpfap.application.model.TransactionDetail;
import org.poc.cpfap.application.repository.TransactionDetailRepository;
import org.poc.cpfap.application.request.TransactionDetailsJSON;
import org.poc.cpfap.application.request.TransactionDtl;
import org.poc.cpfap.application.request.TransactionJSONPostRequest;

@Stateless
public class SampleStatelessBean {
	
	@PersistenceContext
	protected EntityManager em;
	
	TransactionDetailRepository transactionDetailRepository = new TransactionDetailRepository();

	public String hello() {
        return "Hello and Welcome to the DigiBank Transaction application.";
    }
	
	public String getTransactionDetails(String accountNumber) {
		
		List<TransactionDetail> tdList = transactionDetailRepository.findByAccountNumberOrderByIdDesc(Long.valueOf(accountNumber), em);
		List<TransactionDtl> list = appendTransactionToResponse(tdList);
    	TransactionDetailsJSON json = new TransactionDetailsJSON();
    	json.setAccountNumber(accountNumber);
    	json.setTransactions(list);
    	Jsonb jsonb = JsonbBuilder.create();
    	String jsonStr = jsonb.toJson(json);
    	try {
			jsonb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	private List<TransactionDtl> appendTransactionToResponse(List<TransactionDetail> tdList) {
		List<TransactionDtl> list = new ArrayList<>();
		for(TransactionDetail td:tdList) {
    		TransactionDtl tdtl = new TransactionDtl();
    		tdtl.setAmount(String.valueOf(td.getAmount()));
    		tdtl.setStatus(td.getStatus());
    		if(td.getTransactionDateTime()!=null){
    			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        		tdtl.setTransactionDateTime(dateFormat.format(td.getTransactionDateTime()));
    		}
    		tdtl.setTransactionDesc(td.getDescription());
    		tdtl.setTransactionType(td.getTransactionType());
    		tdtl.setTargetAccount(null);
    		tdtl.setTransactionId(td.getTransactionId());
    		list.add(tdtl);
    	}
		return list;
	}
	
	public Boolean insertTransactionDetail(String json_request) {
//		TransactionJSONPostRequest 
		Jsonb jsonb = JsonbBuilder.create();
		TransactionJSONPostRequest jsonRequestObject = jsonb.fromJson(json_request, TransactionJSONPostRequest.class);
    	try {
			jsonb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDetailRepository.insertTransactionDetails(jsonRequestObject, em);
	}
}
