import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function AddBook() {
  const [form, setForm] = useState({
    title: "",
    isbn: "",
    price: "",
    quantity: "",
    author: "", // Changed from authorId to author name string
    imageUrl: "",
    category: "",
    publisher: "",
  });
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const userRole = localStorage.getItem("userRole");
  const isAdmin = userRole === "ADMIN";

  useEffect(() => {
    if (!isAdmin) {
      alert("Access denied. Admin privileges required.");
      navigate("/");
      return;
    }
  }, [isAdmin, navigate]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const bookDTO = {
      title: form.title,
      isbn: form.isbn,
      price: parseFloat(form.price),
      totalCopies: parseInt(form.quantity), // Backend expects totalCopies
      availableCopies: parseInt(form.quantity), // Set available copies too
      author: form.author, // Simple string
      imageUrl: form.imageUrl || "",
      category: form.category || "General", // Simple string
      // publishers: form.publisher ? [form.publisher] : [], // Not in Book entity based on analysis
    };

    try {
      const response = await fetch("/api/books", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bookDTO),
      });

      if (response.ok) {
        setMessage("Book created successfully!");
        setForm({
          title: "",
          isbn: "",
          price: "",
          quantity: "",
          author: "",
          imageUrl: "",
          category: "",
          publisher: "",
        });
        setTimeout(() => {
          navigate("/");
        }, 2000);
      } else {
        const error = await response.json().catch(() => ({}));
        setMessage("Error: " + (error.message || "Failed to create book"));
      }
    } catch (error) {
      setMessage("Network error: " + error.message);
    }
  };

  if (!isAdmin) {
    return null;
  }

  return (
    <div className="min-h-screen bg-app-dark py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto bg-app-surface rounded-lg shadow-xl overflow-hidden border border-gray-700">
        <div className="bg-app-red py-6 px-8">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-white">Add New Book</h1>
            <button
              onClick={() => navigate("/")}
              className="text-white hover:text-gray-200 transition-colors duration-200 flex items-center gap-2 text-sm font-medium"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Back to Home
            </button>
          </div>
        </div>

        <div className="p-8">
          {message && (
            <div className={`mb-6 p-4 rounded-md ${message.includes("Error") ? "bg-red-900/50 text-red-300 border border-red-700" : "bg-green-900/50 text-green-300 border border-green-700"}`}>
              <div className="flex">
                <div className="flex-shrink-0">
                  {message.includes("Error") ? (
                    <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                    </svg>
                  ) : (
                    <svg className="h-5 w-5 text-green-400" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                    </svg>
                  )}
                </div>
                <div className="ml-3">
                  <p className="text-sm font-medium">{message}</p>
                </div>
              </div>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-2">
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-300 mb-1">Title *</label>
                <input
                  type="text"
                  name="title"
                  value={form.title}
                  onChange={handleChange}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="e.g. The Great Gatsby"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">ISBN * (format: xx-xxx-xxx)</label>
                <input
                  type="text"
                  name="isbn"
                  value={form.isbn}
                  onChange={handleChange}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="12-345-678"
                  pattern="\d{2}-\d{3}-\d{3}"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Author *</label>
                <input
                  type="text"
                  name="author"
                  value={form.author}
                  onChange={handleChange}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="Author Name"
                />
              </div>

               <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Category</label>
                <input
                  type="text"
                  name="category"
                  value={form.category}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="Fiction, Sci-Fi..."
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Quantity *</label>
                <input
                  type="number"
                  min="0"
                  name="quantity"
                  value={form.quantity}
                  onChange={handleChange}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="0"
                />
              </div>
                
               <div>
                  <label className="block text-sm font-medium text-gray-300 mb-1">Price (Not used in Backend?)</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    name="price"
                    value={form.price}
                    onChange={handleChange}
                     className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  />
               </div>


              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-300 mb-1">Image URL (optional)</label>
                <input
                  type="url"
                  name="imageUrl"
                  value={form.imageUrl}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                  placeholder="https://example.com/image.jpg"
                />
              </div>

            </div>

            <div className="flex items-center justify-end space-x-4 pt-4 border-t border-gray-700 mt-6">
              <button
                type="button"
                onClick={() => navigate("/")}
                className="bg-gray-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-bold text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="bg-app-red py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-bold text-white hover:bg-app-red-hover focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-app-red"
              >
                Create Book
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AddBook;
