import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Book from "./Book";

function MyBooks() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
  const userId = localStorage.getItem("userId");
  const userRole = localStorage.getItem("userRole");

  useEffect(() => {
    if (!isLoggedIn) {
      alert("Please log in to view your books.");
      navigate("/login");
      return;
    }

    if (userId) {
      fetchMyBooks();
    }
  }, [isLoggedIn, userId, navigate]);

  const fetchMyBooks = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/users/mybooks/${userId}`);
      if (response.ok) {
        const data = await response.json();
        setBooks(Array.isArray(data) ? data : []);
      } else {
        console.error("Failed to fetch books");
        setBooks([]);
      }
    } catch (error) {
      console.error("Error fetching my books:", error);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  };

  if (!isLoggedIn) {
    return null;
  }

  return (
    <div className="min-h-screen bg-app-dark py-8 px-4 sm:px-6 lg:px-8 text-white">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8 border-b border-gray-700 pb-4">
          <h1 className="text-3xl font-extrabold text-white">My Books</h1>
          <button
            onClick={() => navigate("/")}
            className="inline-flex items-center px-4 py-2 border border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-200 bg-app-surface hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
          >
            ← Back to Home
          </button>
        </div>

        {loading ? (
          <div className="flex justify-center items-center h-64">
             <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-app-red"></div>
          </div>
        ) : books.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {books.map((book) => (
              <Book
                key={book.id}
                book={book}
                isLogedIn={isLoggedIn}
                userRole={userRole}
                onPurchaseSuccess={() => {
                  fetchMyBooks();
                }}
              />
            ))}
          </div>
        ) : (
          <div className="text-center py-24 bg-app-surface rounded-lg shadow-sm border border-gray-700">
            <svg className="mx-auto h-12 w-12 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
               <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
            <p className="mt-4 text-lg font-medium text-gray-300">You haven't purchased any books yet.</p>
            <button
               onClick={() => navigate("/")}
               className="mt-6 inline-flex items-center px-4 py-2 border border-transparent text-sm font-bold rounded-md shadow-sm text-white bg-app-red hover:bg-app-red-hover focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-app-red"
            >
              Browse Books
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default MyBooks;
