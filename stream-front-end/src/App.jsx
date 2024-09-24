import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import VideoUpload from './components/VideoUpload'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
     <div className="flex flex-col items-center space-y-8 justify-center p-9">
      <h1 className="text-3xl font-bold text-gray-700 dark:text-gray-50">Video Streaming Application</h1>
      <VideoUpload/>
     </div>
    </>
  )
}

export default App
