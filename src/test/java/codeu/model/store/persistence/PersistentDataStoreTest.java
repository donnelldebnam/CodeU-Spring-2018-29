package codeu.model.store.persistence;

import static codeu.model.data.ModelDataTestHelpers.assertActivityEquals;

import codeu.model.data.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "test_username_one";
    String passwordHashOne = "$2a$10$BNte6sC.qoL4AVjO3Rk8ouY6uFaMnsW8B9NjtHWaDNe8GlQRPRT1S";
    Instant creationOne = Instant.ofEpochMilli(1000);
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, creationOne);

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne, false);

    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo, false);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadAndDeleteMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, false, contentOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, false, contentTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());

    // confirm that we deleted all the messages
    persistentDataStore.deleteFrom(inputMessageOne);
    persistentDataStore.deleteFrom(inputMessageTwo);
    Assert.assertTrue(persistentDataStore.loadActivities().isEmpty());
  }

  @Test
  public void testSaveAndLoadHashtags() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String content1 = "soccer";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Set<String> userSource = new HashSet<>();
    Set<String> convSource = new HashSet<>();
    Hashtag inputHashOne = new Hashtag(idOne, content1, creationOne, userSource, convSource);

    UUID idTwo = UUID.fromString("20000000-2222-3333-4444-555555555555");
    String content2 = "football";
    Instant creationTwo = Instant.ofEpochMilli(1000);
    Hashtag inputHashTwo = new Hashtag(idTwo, content2, creationTwo, userSource, convSource);

    // save
    persistentDataStore.writeThrough(inputHashOne);
    persistentDataStore.writeThrough(inputHashTwo);

    // load
    HashMap<String, Hashtag> resultHashtags = persistentDataStore.loadHashtags();

    // confirm that what we saved matches what we loaded
    Hashtag resultHastagOne = resultHashtags.get(content1);
    Assert.assertEquals(idOne, resultHastagOne.getId());
    Assert.assertEquals(content1, resultHastagOne.getContent());
    Assert.assertEquals(creationOne, resultHastagOne.getCreationTime());

    Hashtag resultHastagtwo = resultHashtags.get(content2);
    Assert.assertEquals(idTwo, resultHastagtwo.getId());
    Assert.assertEquals(content2, resultHastagtwo.getContent());
    Assert.assertEquals(creationTwo, resultHastagtwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadActivities() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID idOwner = UUID.fromString("10000001-2222-3333-4444-555555555555");
    Action action1 = Action.REGISTER_USER;
    Instant creationOne = Instant.ofEpochMilli(1000);
    String thumbnail1 = "test content one";
    Activity inputActivityOne =
        new Activity(idOne, idOwner, action1, true, creationOne, thumbnail1);

    UUID idTwo = UUID.fromString("20000000-2222-3333-4444-555555555555");
    UUID idOwnerTwo = UUID.fromString("20000001-2222-3333-4444-555555555555");
    Action action2 = Action.SEND_MESSAGE;
    Instant creationTwo = Instant.ofEpochMilli(1000);
    String thumbnail2 = "test content two";
    Activity inputActivityTwo =
        new Activity(idTwo, idOwnerTwo, action2, true, creationTwo, thumbnail2);

    // save
    persistentDataStore.writeThrough(inputActivityOne);
    persistentDataStore.writeThrough(inputActivityTwo);

    // load
    List<Activity> resultActivities = persistentDataStore.loadActivities();

    // confirm that what we saved matches what we loaded
    Activity resultActivityOne = resultActivities.get(0);
    assertActivityEquals(inputActivityOne, resultActivityOne);

    Activity resultActivityTwo = resultActivities.get(1);
    assertActivityEquals(inputActivityTwo, resultActivityTwo);

    // confirm that we deleted all the messages
    persistentDataStore.deleteFrom(inputActivityOne);
    persistentDataStore.deleteFrom(inputActivityTwo);
    Assert.assertTrue(persistentDataStore.loadActivities().isEmpty());
  }
}
