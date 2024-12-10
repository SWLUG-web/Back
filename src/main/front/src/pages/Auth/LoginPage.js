import React from 'react'
import LoginComponent from "../../components/Auth/LoginComponent"

function LoginPage() {
  return (
    <div className="flex flex-col h-screen w-full justify-center items-center">
        <div className='text-2xl'>
            <LoginComponent/>
        </div>
    </div>
  )
}

export default LoginPage