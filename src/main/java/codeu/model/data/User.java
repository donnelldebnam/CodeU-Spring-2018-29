// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private String passwordHash;
  private final Instant creation;
  private boolean admin;
  private String email;
  private String aboutMe;
  private Set<String> hashtagSet;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password hash of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.admin = false;
    this.aboutMe = "";
    this.email = "";
    this.hashtagSet = new HashSet<String>();
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the Admin status of the user. */
  public boolean isAdmin() {
    return admin;
  }

  /** Sets the admin status of the user. */
  public void setAdmin(boolean status) {
    this.admin = status;
  }

  /** Returns the about me section of this User. */
  public String getAboutMe() {
    return aboutMe;
  }

  /** Sets the about me String of this User. */
  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  /** Returns the email of this User. */
  public String getEmail() { return email; }

  /** Sets the email of this User. */
  public void setEmail(String email) {
    this.email = email;
  }

  /** Sets the password of this User. */
  public void setPassword(String newPassword) {
    String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    this.passwordHash = password;
  }

  public void addHashtag(String content) {
    this.hashtagSet.add(content);
  }

  public String getHashtagNames() {
    return String.join(", ", this.hashtagSet);
  }

  public Set<String> getHashtagSet() {
    return hashtagSet;
  }

  public void setHashtagSet(HashSet<String> hashtagSet) {
    this.hashtagSet = hashtagSet;
  }
}
