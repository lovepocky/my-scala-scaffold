package spec.longevity.domain

import _root_.longevity.model.annotations.persistent
import longevity.model.ptype.Key

@persistent[SimblDomainModel]
case class User(
                 username: Username,
                 email: Email,
                 fullname: String,
                 profile: Option[UserProfile]) {

  def updateProfile(profile: UserProfile): User = copy(profile = Some(profile))

  def deleteProfile: User = copy(profile = None)

}

object User {
  implicit val usernameKey = primaryKey(props.username)
  implicit val emailKey = key(props.email)
}

/**
  * key val
  */

import longevity.model.annotations.keyVal

@keyVal[SimblDomainModel, User]
case class Username(username: String)

@keyVal[SimblDomainModel, User]
case class Email(email: String)


/**
  * component
  */

import longevity.model.annotations.component

@component[SimblDomainModel]
case class UserProfile(
                        tagline: String,
                        imageUri: Uri,
                        description: Markdown)

@component[SimblDomainModel]
case class Uri(uri: String)

@component[SimblDomainModel]
case class Markdown(markdown: String)