package hu.gfmwa.todoapp.models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import org.squeryl.Query
import org.squeryl.KeyedEntity
import org.squeryl.PersistenceStatus

import java.sql.Timestamp

class Todo(val id: Long, 
		   @Column("userid") val userId: Long,
		   @Column("title") val title: String, 
		   @Column("text") val text: String, 
		   @Column("modified") val modified: Timestamp) extends ScalatraRecord {

  def this() = this(0, 0, "nope", "nope", new Timestamp(0))

}

object TodoDb extends Schema {
	val todos = table[Todo]("gfmwa_todos")
	
	on(todos)(a => declare(
    	a.id is(autoIncremented, primaryKey)))
}

object Todo {

  def create(todo:Todo):Boolean = {
    inTransaction {
      val result = TodoDb.todos.insert(todo)
      if(result.isPersisted) {
        true
      } else {
        false
      }
    }
  }
}

trait ScalatraRecord extends KeyedEntity[Long] with PersistenceStatus {

}