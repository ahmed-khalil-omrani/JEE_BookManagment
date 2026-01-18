import React from "react";
import { useNavigate } from "react-router-dom";

export default function Book({ book, isLogedIn, userRole, onPurchaseSuccess }) {
  const navigate = useNavigate();

  if (!book) return null;

  const userId = localStorage.getItem("userId");
  const isAdmin = userRole === "ADMIN";
  const isOutOfStock = !book.quantity || book.quantity <= 0;

  const handleBuyClick = async () => {
    if (!userId) {
      onPurchaseSuccess(false, book.title, book.id);
      return;
    }

    try {
      const response = await fetch(
        `/api/borrow?bookId=${book.id}&userId=${userId}`,
        { method: "POST" }
      );

      if (response.ok) {
        onPurchaseSuccess(true, book.title, book.id);
      } else {
        const data = await response.json().catch(() => ({}));
        onPurchaseSuccess(false, book.title, book.id);
        console.error("Purchase failed:", data.message || "Unknown error");
      }
    } catch (error) {
      console.error("Network error during book purchase.", error);
      onPurchaseSuccess(false, book.title, book.id);
    }
  };

  const handleDelete = async () => {
    const confirmed = window.confirm(
      `Are you sure you want to delete the book: "${book.title}" (ID: ${book.id})? This action cannot be undone.`
    );
    if (!confirmed) {
      return;
    }

    try {
      // Endpoint: /api/books/{userId}/{id}
      const url = `/api/books/${localStorage.getItem("userId")}/${book.id}`;

      const response = await fetch(url, {
        method: "DELETE",
      });

      if (response.ok || response.status === 204) {
        console.log(`Book ID ${book.id} deleted successfully.`);

        const successMessage = JSON.stringify({
          message: `Livre "${book.title}" (ID: ${book.id}) supprimé avec succès.`,
          type: "success",
        });
        sessionStorage.setItem("transactionFeedback", successMessage);

        window.location.reload();
      } else if (response.status === 404) {
        alert(
          `Deletion failed: Book ID ${book.id} was not found (BookDoesNotExistException).`
        );
      } else {
        const errorText = await response.text();
        console.error("Delete failed:", response.status, errorText);
        alert(
          `Failed to delete book: ${response.status}. Check console for details.`
        );
      }
    } catch (error) {
      console.error("Network error during book deletion.", error);
      alert("Network error occurred during deletion.");
    }
  };

  return (
    <div
      className={`flex flex-col bg-app-surface rounded-lg shadow-lg overflow-hidden transition-all duration-300 hover:shadow-2xl hover:-translate-y-1 ${
        isOutOfStock ? "opacity-75 grayscale" : ""
      }`}
    >
      {/* 1. Image */}
      <div className="relative h-64 bg-gray-800">
        {book.imageUrl ? (
          <img
            src={book.imageUrl}
            alt={`Cover of ${book.title}`}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="flex items-center justify-center h-full text-gray-600">
            <svg
              className="h-16 w-16"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1}
                d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"
              />
            </svg>
          </div>
        )}
        {isOutOfStock && (
          <div className="absolute inset-0 bg-black bg-opacity-70 flex items-center justify-center">
            <span className="text-white font-bold text-lg px-4 py-2 border-2 border-white rounded transform -rotate-12">
              OUT OF STOCK
            </span>
          </div>
        )}
      </div>

      {/* 2. Details */}
      <div className="flex-1 p-4 flex flex-col justify-between">
        <div>
          <div className="text-xs font-bold tracking-wider uppercase text-red-500 mb-1">
            {book.bookCatigories && book.bookCatigories.length > 0
              ? book.bookCatigories.map((c) => c.name ?? c.title).join(", ")
              : "General"}
          </div>
          <h3
            className="text-lg font-black text-white mb-1 line-clamp-2"
            title={book.title}
          >
            {book.title}
          </h3>
          <p className="text-xs text-gray-400 mb-3">
            by{" "}
            <span className="font-semibold text-gray-300">
              {book.author ? book.author : "Unknown"}
            </span>
          </p>
        </div>

        <div className="mt-2 flex items-center justify-between">
          <span className="text-xl font-bold text-blue-400">
            {book.price} €
          </span>
          <span
            className={`text-xs px-2 py-1 rounded-full ${
              isOutOfStock
                ? "bg-red-900 text-red-100"
                : "bg-green-900 text-green-100"
            }`}
          >
            {isOutOfStock ? "Sold Out" : `${book.quantity} left`}
          </span>
        </div>
      </div>

      {/* 3. Actions */}
      <div className="px-4 pb-4">
        <div className="flex space-x-2">
          {isLogedIn && !isOutOfStock && (
            <button
              onClick={handleBuyClick}
              className="flex-1 bg-app-red hover:bg-app-red-hover text-white font-bold py-2 px-4 rounded transition-colors duration-200"
            >
              Buy
            </button>
          )}
          {isAdmin && (
            <>
              <button
                onClick={handleDelete}
                className="flex-1 bg-gray-600 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded transition-colors duration-200"
              >
                Delete
              </button>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/editbook/${book.id}`, { state: { book } });
                }}
                className="flex-1 bg-yellow-600 hover:bg-yellow-700 text-white py-2 rounded-md font-semibold transition-colors duration-200"
              >
                Edit
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
