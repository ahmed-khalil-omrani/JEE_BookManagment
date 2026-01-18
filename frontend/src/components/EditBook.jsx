import { useState, useEffect } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";

function EditBook() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { id } = useParams();

  const [form, setForm] = useState({
    title: "",
    isbn: "",
    price: "",
    quantity: "",
    author: "",
    imageUrl: "",
    category: "",
    publisher: "",
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  const userRole = localStorage.getItem("userRole");
  const isAdmin = userRole === "ADMIN";

  useEffect(() => {
    if (!isAdmin) {
      alert("Access denied. Admin privileges required.");
      navigate("/");
      return;
    }
    
    // Initial data setup
    if (state && state.book) {
        const book = state.book;
        // Check if author is object or string (based on current backend serialization)
        // If it's a string from backend, use it directly. If it was object in old frontend logic, handle safely.
        let authorName = "";
        if (typeof book.author === 'string') {
            authorName = book.author;
        } else if (book.author && book.author.name) {
            authorName = book.author.name;
        }

        setForm({
            title: book.title || "",
            isbn: book.isbn || "",
            price: book.price || "",
            quantity: book.quantity || "", // Or totalCopies if available
            author: authorName,
            imageUrl: book.imageUrl || "",
            category: book.category || "", // String category
             // publisher not present in Entity
        });
        setLoading(false);
    } else {
        setMessage("Error: Book data missing. Please navigate from Home page.");
        setLoading(false);
    }
  }, [isAdmin, navigate, state]);


  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const bookDTO = {
      title: form.title,
      isbn: form.isbn,
      price: parseFloat(form.price),
      totalCopies: parseInt(form.quantity), // Mapping to Backend field name
      availableCopies: parseInt(form.quantity), 
      author: form.author, // String
      imageUrl: form.imageUrl || "",
      category: form.category || "",
      // publishers: not supported
    };

    try {
      const response = await fetch(`/api/books/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bookDTO),
      });

      if (response.ok) {
        setMessage("Book updated successfully!");
        setTimeout(() => {
          navigate("/");
        }, 2000);
      } else {
        const error = await response.json().catch(() => ({}));
        setMessage("Error: " + (error.message || "Failed to update book"));
      }
    } catch (error) {
      setMessage("Network error: " + error.message);
    }
  };

  if (!isAdmin) return null;
  if (loading) return <div className="text-white p-8">Loading...</div>;

  return (
    <div className="min-h-screen bg-app-dark py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto bg-app-surface rounded-lg shadow-xl overflow-hidden border border-gray-700">
        <div className="bg-app-red py-6 px-8">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-white">Edit Book</h1>
            <button
              onClick={() => navigate("/")}
              className="text-white hover:text-gray-200 transition-colors duration-200 flex items-center gap-2 text-sm font-medium"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Cancel
            </button>
          </div>
        </div>

        <div className="p-8">
          {message && (
            <div className={`mb-6 p-4 rounded-md ${message.includes("Error") ? "bg-red-900/50 text-red-300 border border-red-700" : "bg-green-900/50 text-green-300 border border-green-700"}`}>
              <p className="text-sm font-medium">{message}</p>
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
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">ISBN *</label>
                <input
                  type="text"
                  name="isbn"
                  value={form.isbn}
                  onChange={handleChange}
                  required
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
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
                <label className="block text-sm font-medium text-gray-300 mb-1">Price (Not used) *</label>
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  name="price"
                  value={form.price}
                  onChange={handleChange}
                  className="block w-full px-3 py-2 border-transparent rounded-md text-white bg-app-input focus:ring-app-red focus:border-app-red sm:text-sm"
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
                />
              </div>

              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-300 mb-1">Image URL</label>
                <input
                  type="url"
                  name="imageUrl"
                  value={form.imageUrl}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-transparent rounded-md shadow-sm placeholder-gray-500 text-white bg-app-input focus:outline-none focus:ring-app-red focus:border-app-red sm:text-sm"
                />
              </div>
            </div>

            <div className="flex items-center justify-end space-x-4 pt-4 border-t border-gray-700 mt-6">
              <button
                type="button"
                onClick={() => navigate("/")}
                className="bg-gray-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-bold text-white hover:bg-gray-700"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="bg-app-red py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-bold text-white hover:bg-app-red-hover"
              >
                Update Book
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default EditBook;
