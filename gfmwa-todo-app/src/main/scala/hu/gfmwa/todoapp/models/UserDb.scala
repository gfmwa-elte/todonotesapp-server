package hu.gfmwa.todoapp.models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import org.squeryl.Query
import org.squeryl.KeyedEntity
import org.squeryl.PersistenceStatus

import java.sql.Timestamp

class User(val id: Long,
		   @Column("username") val username: String, 
		   @Column("mailaddress") val mailaddress: String, 
		   @Column("password") val password: String) extends ScalatraRecord {

  def this() = this(0, "nope", "nope", "nope")

}

object UserDb extends Schema {
	val users = table[User]("gfmwa_users")
	
	on(users)(a => declare(
    	a.id is(autoIncremented, primaryKey)))
}

object User {

  def create(user:User):Boolean = {
    inTransaction {
      val result = UserDb.users.insert(user)
      if(result.isPersisted) {
        true
      } else {
        false
      }
    }
  }
}
