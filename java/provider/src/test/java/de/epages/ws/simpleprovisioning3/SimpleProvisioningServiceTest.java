package de.epages.ws.simpleprovisioning3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.epages.ws.LocalEpagesConfReader;
import de.epages.ws.ProviderWebServiceTestConfiguration;
import de.epages.ws.common.model.TAttribute;
import de.epages.ws.shopconfig6.ShopConfigServiceClient;
import de.epages.ws.shopconfig6.ShopConfigServiceClientImpl;
import de.epages.ws.simpleprovisioning3.stub.TCreateShop;
import de.epages.ws.simpleprovisioning3.stub.TInfoShop;
import de.epages.ws.simpleprovisioning3.stub.TRename_Input;
import de.epages.ws.simpleprovisioning3.stub.TShopRef;
import de.epages.ws.simpleprovisioning3.stub.TUpdateShop;

public class SimpleProvisioningServiceTest {

    private static final SimpleProvisioningServiceClient simpleProvisioningService = new SimpleProvisioningServiceClientImpl(
            new ProviderWebServiceTestConfiguration());

    private static final ShopConfigServiceClient shopConfigService = new ShopConfigServiceClientImpl(
            new ProviderWebServiceTestConfiguration());

    private static String ALIAS = "java_test-1";
    private static String NEW_ALIAS = "java_test-2";
    private static String DATABASE = "Store"; // default database

    /**
     * Sets all the required prerequisites for the tests. Will be called before
     * the test are run.
     */
    @Before
    public void init() {
        cleanUp();
    }

    //After
    public void cleanUp() {
        // delete the shop if it still/already exists
        deleteIfExists(ALIAS);
        deleteIfExists(NEW_ALIAS);
    }

    private void deleteIfExists(String alias) {
        de.epages.ws.shopconfig6.stub.TShopRef shopRef = new de.epages.ws.shopconfig6.stub.TShopRef();
        shopRef.setAlias(ALIAS);
        if (shopConfigService.exists(shopRef)) {
            shopConfigService.deleteShopRef(shopRef);
        }

        shopRef.setAlias(NEW_ALIAS);
        if (shopConfigService.exists(shopRef)) {
            shopConfigService.deleteShopRef(shopRef);
        }
    }

    @Test
    public void testSuite() {
        // create a shop
        TCreateShop Shop_create = new TCreateShop();
        Shop_create.setAlias(ALIAS);
        Shop_create.setShopType("ECom100");
        Shop_create.setDomainName("mydomain.com");
        Shop_create.setHasSSLCertificate(false);
        Shop_create.setIsClosed(false);
        Shop_create.setIsTrialShop(true);
        Shop_create.setIsInternalTestShop(false);
        Shop_create.setMerchantLogin("max");
        Shop_create.setMerchantPassword("123456");
        Shop_create.setMerchantEMail("max@nowhere.de");
        TAttribute strAttributeChannel = new TAttribute("Channel","mail campaign",null,"String");
        TAttribute strAttributeAquiration = new TAttribute("CountAquiration","7",null,"Integer");
		Shop_create.setAdditionalAttributes( new TAttribute[]{strAttributeChannel,strAttributeAquiration} );

        simpleProvisioningService.create(Shop_create);

        // test if a shop reference exists
        TShopRef shopRef = new TShopRef();
        shopRef.setAlias(ALIAS);

        boolean exists = simpleProvisioningService.exists(shopRef);
        assertTrue("exists?", exists);

        // get information about a shop
        TInfoShop Shop_out = simpleProvisioningService.getInfo(shopRef);

        assertEquals("ShopType", Shop_create.getShopType(), Shop_out.getShopType());
        assertEquals("Database", DATABASE, Shop_out.getDatabase());
        assertEquals("IsClosed", Shop_create.getIsClosed(), Shop_out.isIsClosed());
        assertEquals("IsDeleted", false, Shop_out.isIsDeleted());
        assertEquals("IsTrialShop", Shop_create.getIsTrialShop(), Shop_out.isIsTrialShop());
        assertEquals("IsInternalTestShop", Shop_create.getIsInternalTestShop(), Shop_out.isIsInternalTestShop());
        assertEquals("DomainName", Shop_create.getDomainName(), Shop_out.getDomainName());
        assertEquals("HasSSLCertificate", Shop_create.getHasSSLCertificate(), Shop_out.isHasSSLCertificate());
        assertEquals("MerchantLogin", Shop_create.getMerchantLogin(), Shop_out.getMerchantLogin());
        assertEquals("MerchantEMail", Shop_create.getMerchantEMail(), Shop_out.getMerchantEMail());
        assertNull("LastMerchantLoginDate", Shop_out.getLastMerchantLoginDate());
        assertEquals("IsMarkedForDel", false, Shop_out.isIsMarkedForDel());
        assertEquals("StorefrontURL", "http://" + Shop_create.getDomainName() + "/epages/" + Shop_create.getAlias() + ".sf",
                Shop_out.getStorefrontURL());
        String BackofficeURL = Shop_out.getBackofficeURL();
        //distinguish between provider with and without SSL (AD-7592)
        if (BackofficeURL.startsWith("https")) {
            String ep6HostName = System.getProperty("wsHostName");
            if (ep6HostName == null) {
                ep6HostName = LocalEpagesConfReader.getHostNameFromEpagesConf();
            }
        	assertTrue("BackofficeURL", BackofficeURL.endsWith("://" + ep6HostName
        			+ "/epages/" + Shop_create.getAlias() + ".admin"));
        } else {
	        assertTrue("BackofficeURL", BackofficeURL.endsWith("://" + Shop_create.getDomainName()
	        		+ "/epages/" + Shop_create.getAlias() + ".admin"));
        }
        //check created channel attribute
        assertTrue("additionalAttributes", Shop_out.getAdditionalAttributes().length >= 2 );
        for(TAttribute t: Shop_out.getAdditionalAttributes()) {
        	if( t.getName() == strAttributeChannel.getName() ) {
                assertEquals("channel attribute Name", t.getName(), strAttributeChannel.getName() );
                assertEquals("channel attribute Value", t.getValue(), strAttributeChannel.getValue() );
                assertEquals("channel attribute Type", t.getType(), strAttributeChannel.getType() );
        	} else if ( t.getName() == strAttributeAquiration.getName()) {
                assertEquals("aquiration attribute Name", t.getName(), strAttributeAquiration.getName() );
                assertEquals("aquiration attribute Value", t.getValue(), strAttributeAquiration.getValue() );
                assertEquals("aquiration attribute Type", t.getType(), strAttributeAquiration.getType() );
			}
        }

        // update the shop (all attributes are optional)
        TUpdateShop Shop_update = new TUpdateShop();
        Shop_update.setAlias(ALIAS);
        Shop_update.setShopType("ECom1000");
        Shop_update.setHasSSLCertificate(false);
        Shop_update.setIsTrialShop(false);
        Shop_update.setIsInternalTestShop(true);
        Shop_update.setDomainName("yourdomain.com");
        Shop_update.setMerchantLogin("gabi");
        Shop_update.setMerchantPassword("654321");
        Shop_update.setMerchantEMail("gabi@nowhere.de");
        TAttribute strAttributeSetupFee = new TAttribute("SetupFee","17.33",null,"Float");
        strAttributeChannel.setValue("phone campaign");
        strAttributeChannel.setValue("3");
		Shop_create.setAdditionalAttributes( new TAttribute[]{strAttributeChannel,strAttributeChannel,strAttributeSetupFee} );

        simpleProvisioningService.update(Shop_update);
        // get information about the updated shop
        Shop_out = simpleProvisioningService.getInfo(shopRef);
        assertEquals("updated ShopType", Shop_update.getShopType(), Shop_out.getShopType());
        assertEquals("updated Database", DATABASE, Shop_out.getDatabase());
        assertEquals("updated IsClosed", Shop_create.getIsClosed(), Shop_out.isIsClosed());
        assertFalse("updated IsDeleted", Shop_out.isIsDeleted());
        assertEquals("updated IsTrialShop", Shop_update.getIsTrialShop(), Shop_out.isIsTrialShop());
        assertEquals("updated IsInternalTestShop", Shop_update.getIsInternalTestShop(), Shop_out.isIsInternalTestShop());
        assertEquals("updated DomainName", Shop_update.getDomainName(), Shop_out.getDomainName());
        assertEquals("updated HasSSLCertificate", Shop_update.getHasSSLCertificate(), Shop_out.isHasSSLCertificate());
        assertEquals("updated MerchantLogin", Shop_update.getMerchantLogin(), Shop_out.getMerchantLogin());
        assertEquals("updated MerchantEMail", Shop_update.getMerchantEMail(), Shop_out.getMerchantEMail());
        assertNull("updated LastMerchantLoginDate", Shop_out.getLastMerchantLoginDate());
        assertFalse("updated IsMarkedForDel", Shop_out.isIsMarkedForDel());
        assertEquals("updated StorefrontURL", "http://" + Shop_update.getDomainName() + "/epages/" + ALIAS + ".sf",
                Shop_out.getStorefrontURL());
        //check changed/created attributes
        assertTrue("additionalAttributes", Shop_out.getAdditionalAttributes().length >= 2 );
        for(TAttribute t: Shop_out.getAdditionalAttributes()) {
        	if( t.getName() == strAttributeChannel.getName() ) {
                assertEquals("channel attribute Name", t.getName(), strAttributeChannel.getName() );
                assertEquals("channel attribute Value", t.getValue(), strAttributeChannel.getValue() );
                assertEquals("channel attribute Type", t.getType(), strAttributeChannel.getType() );
        	} else if ( t.getName() == strAttributeAquiration.getName()) {
                assertEquals("aquiration attribute Name", t.getName(), strAttributeAquiration.getName() );
                assertEquals("aquiration attribute Value", t.getValue(), strAttributeAquiration.getValue() );
                assertEquals("aquiration attribute Type", t.getType(), strAttributeAquiration.getType() );
        	} else if ( t.getName() == strAttributeSetupFee.getName()) {
                assertEquals("setup fee attribute Name", t.getName(), strAttributeSetupFee.getName() );
                assertEquals("setup fee attribute Value", t.getValue(), strAttributeSetupFee.getValue() );
                assertEquals("setup fee attribute Type", t.getType(), strAttributeSetupFee.getType() );
			}
        }

        // change the shop alias
        TRename_Input Shop_rename = new TRename_Input();
        Shop_rename.setAlias(ALIAS);
        Shop_rename.setNewAlias(NEW_ALIAS);
        simpleProvisioningService.rename(Shop_rename);
        TShopRef shopRefNew = new TShopRef();
        shopRefNew.setAlias(NEW_ALIAS);
        exists = simpleProvisioningService.exists(shopRefNew);
        assertTrue("new alias exists", exists);
        exists = simpleProvisioningService.exists(shopRef);
        assertFalse("old alias no longer exists", exists);
        Shop_out = simpleProvisioningService.getInfo(shopRefNew);
        assertEquals("renamed StorefrontURL", "http://" + Shop_update.getDomainName() + "/epages/" + NEW_ALIAS + ".sf",
                Shop_out.getStorefrontURL());
        // mark the shop for deletion
        simpleProvisioningService.markForDeletion(shopRefNew);
        // get information about the updated shop
        Shop_out = simpleProvisioningService.getInfo(shopRefNew);
        assertTrue("deleted IsClosed", Shop_out.isIsClosed());
        assertFalse("deleted IsDeleted", Shop_out.isIsDeleted());
        assertTrue("deleted IsMarkedForDel", Shop_out.isIsMarkedForDel());
        deleteIfExists(NEW_ALIAS);
    }

    @Test
    public void testDefaults() {
        // create a new shop with a minimum amount of data
        TCreateShop Shop_create = new TCreateShop();
        Shop_create.setAlias(ALIAS);
        Shop_create.setShopType("ECom100");

        simpleProvisioningService.create(Shop_create);

        TShopRef shopRef = new TShopRef();
        shopRef.setAlias(ALIAS);

        TInfoShop Shop_out = simpleProvisioningService.getInfo(shopRef);

        assertEquals("ShopType", Shop_create.getShopType(), Shop_out.getShopType());
        assertEquals("default Database", DATABASE, Shop_out.getDatabase());
        assertFalse("default IsClosed", Shop_out.isIsClosed());
        assertFalse("default IsDeleted", Shop_out.isIsDeleted());
        assertFalse("default IsTrialShop", Shop_out.isIsTrialShop());
        assertFalse("default IsInternalTestShop", Shop_out.isIsInternalTestShop());
        assertNull("default DomainName", Shop_out.getDomainName());
        assertFalse("default HasSSLCertificate", Shop_out.isHasSSLCertificate());
        assertEquals("default MerchantLogin", "admin", Shop_out.getMerchantLogin());
        assertNull("default MerchantEMail", Shop_out.getMerchantEMail());
        assertNull("default LastMerchantLoginDate", Shop_out.getLastMerchantLoginDate());
        assertFalse("default IsMarkedForDel", Shop_out.isIsMarkedForDel());
        assertTrue("default StorefrontURL", Shop_out.getStorefrontURL().endsWith("/epages/" + Shop_create.getAlias() + ".sf"));

        deleteIfExists(ALIAS);
    }
}
