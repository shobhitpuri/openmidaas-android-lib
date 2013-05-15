/*******************************************************************************
 * Copyright 2013 SecureKey Technologies Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.openmidaas.library.persistence;

import java.util.ArrayList;
import java.util.List;

import org.openmidaas.library.MIDaaS;
import org.openmidaas.library.common.Constants;
import org.openmidaas.library.model.AddressAttribute;
import org.openmidaas.library.model.CreditCardAttribute;
import org.openmidaas.library.model.EmailAttribute;
import org.openmidaas.library.model.GenericAttribute;
import org.openmidaas.library.model.InvalidAttributeNameException;
import org.openmidaas.library.model.InvalidAttributeValueException;
import org.openmidaas.library.model.PhoneAttribute;
import org.openmidaas.library.model.SubjectToken;
import org.openmidaas.library.model.core.AbstractAttribute;
import org.openmidaas.library.model.core.MIDaaSError;
import org.openmidaas.library.model.core.MIDaaSException;
import org.openmidaas.library.persistence.core.AddressDataCallback;
import org.openmidaas.library.persistence.core.AttributeDataCallback;
import org.openmidaas.library.persistence.core.AttributePersistenceDelegate;
import org.openmidaas.library.persistence.core.CreditCardDataCallback;
import org.openmidaas.library.persistence.core.EmailDataCallback;
import org.openmidaas.library.persistence.core.GenericDataCallback;
import org.openmidaas.library.persistence.core.PhoneNumberDataCallback;
import org.openmidaas.library.persistence.core.SubjectTokenCallback;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * 
 * Delegate that persists attribute data to a SQLite database. 
 *
 */
public class AttributeDBPersistence implements AttributePersistenceDelegate{
	
	/**
	 * @uml.property  name="tAG"
	 */
	private final String TAG = "AttributeDBPersistenceDelegate";

	/**
	 * @uml.property  name="database"
	 * @uml.associationEnd  
	 */
	private SQLiteDatabase database;
	
	/**
	 * @uml.property  name="dbHelper"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private AttributeDBHelper dbHelper;
	
	/**
	 * @uml.property  name="mSubjectTokenBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SubjectTokenDBBuilder mSubjectTokenBuilder;
	
	/**
	 * @uml.property  name="mEmailBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private EmailDBBuilder mEmailBuilder;
	
	/**
	 * @uml.property  name="mGenericBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GenericDBBuilder mGenericBuilder;
	
	/**
	 * @uml.property  name="mShippingAddressBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private AddressDBBuilder mShippingAddressBuilder;
	
	/**
	 * @uml.property  name="mCreditCardDBBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CreditCardDBBuilder mCreditCardDBBuilder;
	
	/**
	 * @uml.property  name="mPhoneNumberBuilder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private PhoneNumberDBBuilder mPhoneNumberBuilder;
	
	public AttributeDBPersistence(){
		dbHelper = AttributeDBHelper.getInstance();
		mSubjectTokenBuilder = new SubjectTokenDBBuilder();
		mEmailBuilder = new EmailDBBuilder();
		mGenericBuilder = new GenericDBBuilder();
		mShippingAddressBuilder = new AddressDBBuilder();
		mCreditCardDBBuilder = new CreditCardDBBuilder();
		mPhoneNumberBuilder = new PhoneNumberDBBuilder();
	}
	
	@Override
	public boolean save(final AbstractAttribute<?> data) throws MIDaaSException {
		boolean val = false;
			try {
				database = dbHelper.getWritableDatabase();
				// id we don't have a PK, create a row in the db.
				if(data.getId() == -1) {
					long rowId = database.insertOrThrow(AttributesTable.TABLE_NAME, null, getContentValuesForAttribute(data));
					if(rowId == -1) {
						// error saving record;
						// TODO: Retry save. keep a watchdog timer/check on the status of the operation
						MIDaaS.logError(TAG, "could not save attribute to the database. Returning false");
						val = false;
					} else {
						MIDaaS.logDebug(TAG, "saving new value: " + data.getName());
						data.setId(rowId);
						val =  true;
					}
				}
				// id we have a PK, update the corresponding row. 
				else {
					MIDaaS.logDebug(TAG, "updating previous value: " + data.getName());
					int v = database.update(AttributesTable.TABLE_NAME, getContentValuesForAttribute(data), "_id =" + data.getId(), null);
					// only 1 row should be updated!
					if (v == 1) {
						MIDaaS.logDebug(TAG, "update successful");
						val = true;
					}
				}
			} catch (SQLiteConstraintException exception) {
				MIDaaS.logError(TAG, exception.getMessage());
				throw new MIDaaSException(MIDaaSError.ATTRIBUTE_ALREADY_EXISTS);
				
			} catch(Exception e) {
				MIDaaS.logError(TAG, e.getMessage());
				throw new MIDaaSException(MIDaaSError.DATABASE_ERROR);
			} finally {
				dbHelper.close();
			}
			return val;
	}

	@Override
	public boolean delete(final AbstractAttribute<?> data) throws MIDaaSException {
		boolean val = false;
		try {
			database = dbHelper.getWritableDatabase();
			if (data.getId() != -1) {
				MIDaaS.logDebug(TAG, "deleting: " + data.getName());
				database.delete(AttributesTable.TABLE_NAME, "_id = " + data.getId(), null);
				dbHelper.close();
				val = true;
			} else {
				val = true;
			}
		} catch(Exception e) {
			MIDaaS.logError(TAG, e.getMessage());
			throw new MIDaaSException(MIDaaSError.DATABASE_ERROR);
		} finally {
			dbHelper.close();
		}
		return val;
	}
	
	@Override
	public void getAllAttributes(final AttributeDataCallback callback) {
		try {
			List<AbstractAttribute<?>> mList = new ArrayList<AbstractAttribute<?>>();
			List<EmailAttribute> emailList = 
					this.<EmailAttribute>getAttributeFor(Constants.RESERVED_WORDS.email.toString(), this.mEmailBuilder);
			List<AddressAttribute> shippingAddressList = 
					this.<AddressAttribute>getAttributeFor(Constants.RESERVED_WORDS.address.toString(), this.mShippingAddressBuilder);
			List<CreditCardAttribute> creditCardList = 
					this.<CreditCardAttribute>getAttributeFor(Constants.RESERVED_WORDS.credit_card.toString(), this.mCreditCardDBBuilder);
			List<PhoneAttribute> phoneNumberList = 
					this.<PhoneAttribute>getAttributeFor(Constants.RESERVED_WORDS.phone.toString(), this.mPhoneNumberBuilder);
			List<GenericAttribute> genericList = new ArrayList<GenericAttribute>();
			
			mList.addAll(emailList);
			mList.addAll(shippingAddressList);
			mList.addAll(creditCardList);
			mList.addAll(phoneNumberList);
			
			// get all generic attributes
			List<String> genericAttributeNames = getAllGenericAttributeNames();
			for(String name: genericAttributeNames) {
				this.mGenericBuilder.setName(name);
				genericList.addAll(this.getAttributeFor(name, this.mGenericBuilder));
			}
			mList.addAll(genericList);
			callback.onSuccess(mList);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		} 
	}
	
	@Override
	public void getEmails(final EmailDataCallback callback) {
		try {
			List<EmailAttribute> list = 
					this.<EmailAttribute>getAttributeFor(Constants.RESERVED_WORDS.email.toString(), this.mEmailBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		}
	}
	
	@Override
	public void getPhoneNumbers(PhoneNumberDataCallback callback) {
		try {
			List<PhoneAttribute> list = 
					this.<PhoneAttribute>getAttributeFor(Constants.RESERVED_WORDS.phone.toString(), this.mPhoneNumberBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		}
		
	}
	
	@Override
	public void getGenerics(final String attributeName, final GenericDataCallback callback) {
		try {
			this.mGenericBuilder.setName(attributeName);
			List<GenericAttribute> list = 
					this.<GenericAttribute>getAttributeFor(attributeName, this.mGenericBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		}
	}

	@Override
	public void getSubjectToken(final SubjectTokenCallback callback) {
		try {
			List<SubjectToken> list = 
					this.<SubjectToken>getAttributeFor(Constants.RESERVED_WORDS.subject_token.toString(), this.mSubjectTokenBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		}
	}
	
	@Override
	public void getAddresses(final AddressDataCallback callback) {
		try {
			List<AddressAttribute> list = 
					this.<AddressAttribute>getAttributeFor(Constants.RESERVED_WORDS.address.toString(), this.mShippingAddressBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		} catch (SQLiteException e) {
			MIDaaS.logError(TAG, e.getMessage());
		}
	}
	
	@Override
	public void getCreditCards(final CreditCardDataCallback callback) {
		try {
			List<CreditCardAttribute> list = 
					this.<CreditCardAttribute>getAttributeFor(Constants.RESERVED_WORDS.credit_card.toString(), this.mCreditCardDBBuilder);
			callback.onSuccess(list);
		} catch (InvalidAttributeNameException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_NAME_ERROR));
		} catch (InvalidAttributeValueException e) {
			MIDaaS.logError(TAG, e.getMessage());
			callback.onError(new MIDaaSException(MIDaaSError.ATTRIBUTE_VALUE_ERROR));
		} catch (MIDaaSException e) {
			MIDaaS.logError(TAG, e.getError().getErrorMessage());
			callback.onError(new MIDaaSException(MIDaaSError.DATABASE_ERROR));
		}
	}
	
	
	/**
	 * Helper function to get content values object from attribute data. 
	 * @param attribute -  the attribute data to persist. 
	 * @return the content values object that is eventually stored in the DB.
	 */
	private ContentValues getContentValuesForAttribute(AbstractAttribute<?> attribute) throws Exception {
		ContentValues contentValues = new ContentValues();
		contentValues.put(AttributesTable.COLUMN_NAME_NAME, attribute.getName());
		contentValues.put(AttributesTable.COLUMN_NAME_LABEL, attribute.getLabel());
		if(attribute.getValue().toString() != null) {
			contentValues.put(AttributesTable.COLUMN_NAME_VALUE, attribute.getValue().toString());
		} else {
			// should never get to this point. All checks for the value should be done prior to this. 
			throw new Exception();
		}
		contentValues.put(AttributesTable.COLUMN_NAME_TOKEN, attribute.getSignedToken());
		contentValues.put(AttributesTable.COLUMN_NAME_PENDING, attribute.getPendingData());
		if(attribute.getLabel() == null) {
			contentValues.putNull(AttributesTable.COLUMN_NAME_LABEL);
		}
		if(attribute.getSignedToken() == null) {
			contentValues.putNull(AttributesTable.COLUMN_NAME_TOKEN);
		} 
		if(attribute.getPendingData() == null) {
			contentValues.putNull(AttributesTable.COLUMN_NAME_PENDING);
		}
		return contentValues;
	}
	
	/**
	 * Helper method to search for an attribute by name. 
	 * @param name the name of the attribute
	 * @return a cursor pointing to the record list. 
	 * @throws MIDaaSException 
	 */
	private Cursor fetchByAttributeName(String name) throws SQLiteException {
		database = dbHelper.getWritableDatabase();
		Cursor cursor = database.query(AttributesTable.TABLE_NAME, null, "name=?", new String[] { name }, null, null, null);
		return cursor;
	}
	
	/**
	 * Helper method to build a attribute list from a cursor. 
	 * @param attributeName - the attribute name to be retrieved
	 * @param builder - the attribute builder
	 * @return an attribute list of type T
	 * @throws InvalidAttributeNameException
	 * @throws InvalidAttributeValueException
	 * @throws MIDaaSException
	 */
	private <T extends AbstractAttribute<?>> List<T> getAttributeFor(String attributeName, AbstractAttributeDBBuilder<T> builder) throws 
	InvalidAttributeNameException, InvalidAttributeValueException, MIDaaSException {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			MIDaaS.logDebug(TAG, "fetching data for: " + attributeName);
			cursor = fetchByAttributeName(attributeName);
			if(cursor == null) {
				MIDaaS.logError(TAG, "Cursor is null. Unable to retrieve data from persistence store. ");
				throw new MIDaaSException(MIDaaSError.DATABASE_ERROR);
			}
			else if(cursor.moveToFirst()) {
				while(!(cursor.isAfterLast())) {
					list.add(builder.buildFromCursor(cursor));
					cursor.moveToNext();
				}
				cursor.close();
				dbHelper.close();
				return list;
			} else {
				cursor.close();
				dbHelper.close();
				return list;
			}
		} catch(SQLiteException e) {
			MIDaaS.logError(TAG, e.getMessage());
			throw new MIDaaSException(MIDaaSError.DATABASE_ERROR);
		} finally {
			if(!cursor.isClosed()) {
				cursor.close();
			}
			dbHelper.close();
		}
	}
	
	/**
	 * Helper method to get all generic attribute names currently 
	 * stored in the DB
	 * @return a list of all generic attribute names
	 */
	private List<String> getAllGenericAttributeNames() {
		Cursor cursor;
		List<String> attributeNames = new ArrayList<String>();
		database = dbHelper.getWritableDatabase();
		// filter out by reserved words
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		for(String reservedName:Constants.getReservedWordsAsList()) {
			if(builder.length() > 1)
				builder.append(',');
			builder.append('\'');
			builder.append(reservedName);
			builder.append('\'');
			
		}
		builder.append(')');
		// get all generic attribute names
		cursor = database.query(AttributesTable.TABLE_NAME, new String[] { "name" }, "name NOT IN "+builder.toString(),null , null, null, null);
		if(cursor != null && cursor.moveToFirst()) {
			while(!(cursor.isAfterLast())) {
				if(!(attributeNames.contains(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_NAME))))) {
					attributeNames.add(cursor.getString(cursor.getColumnIndex(AttributesTable.COLUMN_NAME_NAME)));
				}
				cursor.moveToNext();
			}
			cursor.close();
			dbHelper.close();
			return attributeNames;
		} else {
			cursor.close();
			dbHelper.close();
			return attributeNames;
		}
	}
}
