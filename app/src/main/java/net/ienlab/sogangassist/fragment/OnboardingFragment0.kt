package net.ienlab.sogangassist.fragment

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import net.ienlab.sogangassist.databinding.FragmentOnboarding0Binding
import net.ienlab.sogangassist.R

class OnboardingFragment0 : Fragment() {

    lateinit var binding: FragmentOnboarding0Binding
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding0, container, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gmSansBold = Typeface.createFromAsset(requireContext().assets, "fonts/gmsans_bold.otf")
        val gmSansMedium = Typeface.createFromAsset(requireContext().assets, "fonts/gmsans_medium.otf")

        binding.className.typeface = gmSansBold
        binding.className2.typeface = gmSansBold
        binding.subName.typeface = gmSansBold
        binding.subName2.typeface = gmSansBold
        binding.endTime.typeface = gmSansMedium
        binding.endTime2.typeface = gmSansMedium
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment0().apply {
            val args = Bundle()
            arguments = args
        }
    }
}