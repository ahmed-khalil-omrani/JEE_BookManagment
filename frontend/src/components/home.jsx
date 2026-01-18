import { useEffect, useState, useCallback } from "react";
import { Link, useSearchParams } from "react-router-dom";
import Book from "./Book";

function Home() {
  const [booksList, setBooksList] = useState([]);
  const [authorsList, setAuthorsList] = useState([]);
  const [selectedAuthor, setSelectedAuthor] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const searchQuery = searchParams.get("search");

  // States for transaction feedback
  const [transactionMessage, setTransactionMessage] = useState(null);
  const [messageType, setMessageType] = useState(""); // 'success' or 'error'

  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
  const userRole = localStorage.getItem("userRole");

  // 1. Fetch Authors on Mount - REMOVED (Authors are strings in Book entity now)
  // Instead we will extract authors from the books list
  useEffect(() => {
    // No separate author fetch needed
  }, []);

  // 2. Function to fetch books (all, by query, or by author)
  const fetchBooks = useCallback(async (query = "", authorName = "") => {
    let url = "/api/books";

    if (authorName) {
      url = `/api/books/author/${encodeURIComponent(authorName)}`;
    } else if (query) {
      url = `/api/books/search?q=${encodeURIComponent(
        query
      )}`;
    }

    try {
      const response = await fetch(url);

      // Handle specific errors
      if (response.status === 404) {
        setBooksList([]);
        setTransactionMessage(
          authorName
            ? "No books found for this author."
            : `No book found with the title: "${query}"`
        );
        setMessageType("error");
        setTimeout(() => setTransactionMessage(null), 5000);
        return;
      }

      if (!response.ok) throw new Error("Network response was not ok");

      const data = await response.json();

      // Ensure data is treated as an array
      const listData = Array.isArray(data) ? data : data ? [data] : [];


      setBooksList(listData);

      // Extract authors from books list for filter
      const authors = [...new Set(listData.map(book => book.author))].filter(Boolean).sort();
      setAuthorsList(authors.map(name => ({ id: name, name: name }))); // Mock object structure for compatibility

      // Feedback for search/filter
      if (authorName && listData.length > 0) {
        // Optional: distinct message for author filter
      } else if (query && listData.length > 0) {
        setTransactionMessage(
          `Search successful. Found ${listData.length} book(s) matching "${query}".`
        );
        setMessageType("success");
        setTimeout(() => setTransactionMessage(null), 5000);
      }
    } catch (error) {
      console.error(`Failed to fetch books:`, error);
      setBooksList([]);
      setTransactionMessage("Failed to connect to the book database.");
      setMessageType("error");
      setTimeout(() => setTransactionMessage(null), 5000);
    }
  }, []);

  // 3. Effect hook for initial setup and search/filter changes
  useEffect(() => {
    // If search query is present in URL, priority is search query.
    // If we select an author, we might want to clear search query or just handle it.
    // Simple logic: If there is a search query in URL, use it. Otherwise use selectedAuthor.
    // However, selecting an author should probably clear the search query to avoid confusion.

    if (searchQuery) {
      if (selectedAuthor !== "") setSelectedAuthor(""); // Sync state
      fetchBooks(searchQuery, "");
    } else {
      fetchBooks("", selectedAuthor);
    }

    // --- Persistence Logic (for Delete Success Message) ---
    const savedFeedback = sessionStorage.getItem("transactionFeedback");
    if (savedFeedback) {
      try {
        const feedback = JSON.parse(savedFeedback);
        setTransactionMessage(feedback.message);
        setMessageType(feedback.type);

        sessionStorage.removeItem("transactionFeedback"); // Clear message after reading

        setTimeout(() => setTransactionMessage(null), 5000);
      } catch (error) {
        console.error("Failed to parse transaction feedback:", error);
        sessionStorage.removeItem("transactionFeedback");
      }
    }
  }, [fetchBooks, searchQuery, selectedAuthor]);

  const handleAuthorChange = (e) => {
    const authorName = e.target.value;
    setSelectedAuthor(authorName);
    if (authorName) {
      // Clear search param if an author is selected to switch mode
      setSearchParams({});
    }
  };

  // 4. Handlers for purchase
  const handlePurchaseComplete = (success, title, bookId) => {
    if (success) {
      const msg = `Achat effectué avec succès! "${title}" a été acheté.`;
      setTransactionMessage(msg);
      setMessageType("success");

      fetchBooks(searchQuery || "", selectedAuthor); // Refresh list to update quantity
      setTimeout(() => setTransactionMessage(null), 5000);
    } else {
      const msg = `Erreur lors de l'achat du livre ${bookId}.`;
      setTransactionMessage(msg);
      setMessageType("error");

      setTimeout(() => setTransactionMessage(null), 5000);
    }
  };

  return (
    <div className="bg-app-dark min-h-screen text-white">
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Admin Quick Links */}
        {isLoggedIn && userRole === "ADMIN" && (
          <div className="mb-8 flex space-x-4 justify-end">
            <Link
              to="/addbook"
              className="bg-app-red hover:bg-app-red-hover text-white px-4 py-2 rounded-md text-sm font-medium shadow-sm transition-colors duration-200"
            >
              + Add Book
            </Link>
            <Link
              to="/addauthor"
              className="bg-app-surface border border-gray-600 hover:bg-gray-700 text-gray-200 px-4 py-2 rounded-md text-sm font-medium shadow-sm transition-colors duration-200"
            >
              + Add Author
            </Link>
          </div>
        )}

        {/* Hero Section */}
        <section className="bg-app-surface rounded-2xl shadow-xl overflow-hidden mb-12 relative h-80 flex items-center">
          {/* Background Image */}
          <div className="absolute inset-0 z-0">
            <img
              src="/bib3.jpg"
              alt="Library"
              className="w-full h-full object-cover opacity-60"
            />
            <div className="absolute inset-0 bg-gradient-to-r from-black via-black/70 to-transparent"></div>
          </div>

          <div className="relative z-10 px-8 sm:px-16 text-left max-w-2xl">
            <h1 className="text-5xl font-extrabold tracking-tight text-white mb-4 drop-shadow-lg">
              WELCOME TO THE LIBRARY
            </h1>
            <h2 className="text-2xl font-semibold text-gray-300 mb-6 italic">
              "So many books, so little time"
            </h2>
            <div className="text-white space-y-2">
              <h3 className="text-xl">
                Libraries{" "}
                <strong className="text-white">
                  store the energy that fuels the imagination
                </strong>
              </h3>
              <p className="text-gray-400 mt-4">
                Your one-stop destination for all your reading needs.
              </p>
            </div>
          </div>
        </section>

        <section className="mb-12">
          {/* Header & Filter Row */}
          <div className="flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
            <div className="flex items-center">
              <h2 className="text-2xl font-bold text-white border-l-4 border-app-red pl-4">
                {searchQuery
                  ? `Search Results for "${searchQuery}"`
                  : selectedAuthor
                  ? "Books by Selected Author"
                  : "Available Books"}
              </h2>
            </div>

            {/* Author Filter Dropdown */}
            <div className="flex items-center space-x-2">
              <span className="text-sm text-gray-400">Filter by Author:</span>
              <select
                value={selectedAuthor}
                onChange={handleAuthorChange}
                className="bg-app-surface border border-gray-600 text-white text-sm rounded-md px-3 py-1.5 focus:outline-none focus:border-app-red"
              >
                <option value="">All Authors</option>
                {authorsList.map((author) => (
                  <option key={author.id} value={author.id}>
                    {author.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {!searchQuery && !selectedAuthor && (
            <div className="mb-4 text-sm text-gray-500">Browsing all books</div>
          )}

          {/* Display Global Transaction Message */}
          {transactionMessage && (
            <div
              className={`mb-6 p-4 rounded-md border ${
                messageType === "success"
                  ? "bg-green-900/50 text-green-300 border-green-700"
                  : "bg-red-900/50 text-red-300 border-red-700"
              } flex items-center shadow-sm`}
            >
              <span className="mr-3 text-2xl">
                {messageType === "success" ? "✓" : "⚠"}
              </span>
              {transactionMessage}
            </div>
          )}

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
            {booksList.length > 0 ? (
              booksList.map((book) => (
                <Book
                  key={book.id}
                  book={book}
                  isLogedIn={isLoggedIn}
                  userRole={userRole}
                  onPurchaseSuccess={handlePurchaseComplete}
                />
              ))
            ) : (
              <div className="col-span-full text-center py-20 bg-app-surface rounded-lg shadow-sm">
                <p className="text-xl text-gray-500">
                  {searchQuery
                    ? `No results found for "${searchQuery}".`
                    : selectedAuthor
                    ? "No books found for this author."
                    : "No books available yet."}
                </p>
                {(searchQuery || selectedAuthor) && (
                  <button
                    onClick={() => {
                      setSearchParams({});
                      setSelectedAuthor("");
                    }}
                    className="mt-4 text-app-red hover:text-white underline"
                  >
                    Clear filters
                  </button>
                )}
              </div>
            )}
          </div>
        </section>
      </main>

      <footer className="bg-app-surface text-gray-400 py-8 border-t border-gray-700">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <p className="mb-4">&copy; 2025 Library. All rights reserved.</p>
          <div className="space-x-4 text-sm">
            <Link
              to="/public/terms"
              className="hover:text-app-red transition-colors"
            >
              Terms of Service
            </Link>
            <span className="text-gray-600">|</span>
            <Link
              to="/public/privacy"
              className="hover:text-app-red transition-colors"
            >
              Privacy Policy
            </Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Home;
