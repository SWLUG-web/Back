import React, {Suspense, useEffect} from "react";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import Header from './components/Header';
import Footer from './components/Footer';
import { Provider } from 'react-redux';
import store from './store/store';

import "./styles/common.css";

function App() {

  return (
      <Provider store={store}>
          <div className="App flex flex-col min-h-screen">
              <Header/>
              <main className="min-h-screen">
                  <div className="container mx-auto text-center">
                      <Suspense fallback={<div>Loading...</div>}>
                          <RouterProvider router={root}/>
                      </Suspense>
                  </div>
              </main>
              <Footer/>
          </div>
      </Provider>
  );
}

export default App;
