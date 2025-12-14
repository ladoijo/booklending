package com.library.booklending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.library.booklending.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Query("""
        UPDATE Book AS book
        SET book.isDeleted = :isDelete
        WHERE book.id = :id
    """ )
    void updateDeleteFlagById(Long id, boolean isDelete);

    @Query("""
        SELECT CASE WHEN book.availableCopies > 0 THEN TRUE ELSE FALSE END
        FROM Book book
        WHERE book.id = :id
            AND book.isDeleted = false
    """)
    boolean isBookAvailableById(Long id);

    @Modifying
    @Query("update Book b set b.availableCopies = b.availableCopies - 1 where b.id = :id and b.availableCopies > 0")
    int decrementAvailableCopies(Long id);

    @Modifying
    @Query("update Book b set b.availableCopies = b.availableCopies + 1 where b.id = :id and b.availableCopies < b.totalCopies")
    int incrementAvailableCopies(Long id);
}
